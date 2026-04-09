package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlateBlock extends Block implements SimpleWaterloggedBlock {

    public static final MapCodec<PlateBlock> CODEC = simpleCodec(PlateBlock::new);

    // --- VoxelShape constants for getShape() ---
    // Straight: 8-pixel-deep half-block against each face
    private static final VoxelShape S_STRAIGHT = Block.box( 0, 0,  8, 16, 16, 16);
    private static final VoxelShape N_STRAIGHT = Block.box( 0, 0,  0, 16, 16,  8);
    private static final VoxelShape E_STRAIGHT = Block.box( 8, 0,  0, 16, 16, 16);
    private static final VoxelShape W_STRAIGHT = Block.box( 0, 0,  0,  8, 16, 16);
    // Quarter-corner pieces (8×8 footprint, full height)
    private static final VoxelShape NW_CORNER  = Block.box( 0, 0,  0,  8, 16,  8);
    private static final VoxelShape NE_CORNER  = Block.box( 8, 0,  0, 16, 16,  8);
    private static final VoxelShape SW_CORNER  = Block.box( 0, 0,  8,  8, 16, 16);
    private static final VoxelShape SE_CORNER  = Block.box( 8, 0,  8, 16, 16, 16);
    // Inner-corner L-shapes (straight + perpendicular quarter)
    private static final VoxelShape INNER_A    = Shapes.or(S_STRAIGHT, NW_CORNER); // S + NW
    private static final VoxelShape INNER_B    = Shapes.or(E_STRAIGHT, SW_CORNER); // E + SW
    private static final VoxelShape INNER_C    = Shapes.or(N_STRAIGHT, SE_CORNER); // N + SE
    private static final VoxelShape INNER_D    = Shapes.or(W_STRAIGHT, NE_CORNER); // W + NE
    // Lookup [direction.get2DDataValue() S=0,W=1,N=2,E=3][StairsShape.ordinal() STRAIGHT=0,INNER_LEFT=1,INNER_RIGHT=2,OUTER_LEFT=3,OUTER_RIGHT=4]
    private static final VoxelShape[][] PLATE_SHAPES = {
        { S_STRAIGHT, INNER_B, INNER_A, SE_CORNER, SW_CORNER }, // SOUTH
        { W_STRAIGHT, INNER_A, INNER_D, SW_CORNER, NW_CORNER }, // WEST
        { N_STRAIGHT, INNER_D, INNER_C, NW_CORNER, NE_CORNER }, // NORTH
        { E_STRAIGHT, INNER_C, INNER_B, NE_CORNER, SE_CORNER }, // EAST
    };
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PlateBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SHAPE, StairsShape.STRAIGHT)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Direction facing = ctx.getHorizontalDirection();
        FluidState fluid = ctx.getLevel().getFluidState(pos);
        BlockState state = this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        return state.setValue(SHAPE, computeShape(state, ctx.getLevel(), pos));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction.getAxis().isHorizontal()) {
            return state.setValue(SHAPE, computeShape(state, level, pos));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private StairsShape computeShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction facing = state.getValue(FACING);

        // Outer corners: check the block in the facing direction
        BlockState front = level.getBlockState(pos.relative(facing));
        if (front.getBlock() instanceof PlateBlock) {
            Direction frontFacing = front.getValue(FACING);
            if (frontFacing.getAxis() != facing.getAxis()) {
                if (isDifferentPlate(state, level, pos, frontFacing.getOpposite())) {
                    return frontFacing == facing.getCounterClockWise()
                            ? StairsShape.OUTER_LEFT : StairsShape.OUTER_RIGHT;
                }
            }
        }

        // Inner corners: check the block opposite the facing direction
        BlockState back = level.getBlockState(pos.relative(facing.getOpposite()));
        if (back.getBlock() instanceof PlateBlock) {
            Direction backFacing = back.getValue(FACING);
            if (backFacing.getAxis() != facing.getAxis()) {
                if (isDifferentPlate(state, level, pos, backFacing)) {
                    return backFacing == facing.getCounterClockWise()
                            ? StairsShape.INNER_LEFT : StairsShape.INNER_RIGHT;
                }
            }
        }

        return StairsShape.STRAIGHT;
    }

    /** Returns true if the block at pos+dir is NOT a same-facing plate (i.e. it won't continue our wall). */
    private boolean isDifferentPlate(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        BlockState neighbor = level.getBlockState(pos.relative(dir));
        if (!(neighbor.getBlock() instanceof PlateBlock)) return true;
        return neighbor.getValue(FACING) != state.getValue(FACING);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        Direction facing = state.getValue(FACING);
        StairsShape shape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (facing.getAxis() == Direction.Axis.Z) {
                    return state.setValue(FACING, mirror.mirror(facing))
                            .setValue(SHAPE, mirrorShape(shape));
                }
                break;
            case FRONT_BACK:
                if (facing.getAxis() == Direction.Axis.X) {
                    return state.setValue(FACING, mirror.mirror(facing))
                            .setValue(SHAPE, mirrorShape(shape));
                }
                break;
            default:
                break;
        }
        return super.mirror(state, mirror);
    }

    private StairsShape mirrorShape(StairsShape shape) {
        return switch (shape) {
            case INNER_LEFT  -> StairsShape.INNER_RIGHT;
            case INNER_RIGHT -> StairsShape.INNER_LEFT;
            case OUTER_LEFT  -> StairsShape.OUTER_RIGHT;
            case OUTER_RIGHT -> StairsShape.OUTER_LEFT;
            default          -> shape;
        };
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return PLATE_SHAPES[state.getValue(FACING).get2DDataValue()][state.getValue(SHAPE).ordinal()];
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE, WATERLOGGED);
    }
}
