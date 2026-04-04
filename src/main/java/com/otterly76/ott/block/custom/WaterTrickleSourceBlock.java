package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaterTrickleSourceBlock extends DirectionalBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<WaterTrickleSourceBlock> CODEC = simpleCodec(WaterTrickleSourceBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty CONNECTED_ABOVE = BooleanProperty.create("connected_above");

    private static final VoxelShape SHAPE_NORTH = Block.box(6, 0,  0, 10, 16,  5);
    private static final VoxelShape SHAPE_SOUTH = Block.box(6, 0, 11, 10, 16, 16);
    private static final VoxelShape SHAPE_EAST  = Block.box(11, 0, 6, 16, 16, 10);
    private static final VoxelShape SHAPE_WEST  = Block.box( 0, 0, 6,  5, 16, 10);
    private static final VoxelShape SHAPE_DOWN  = Block.box(6, 11, 6, 10, 16, 10);

    public WaterTrickleSourceBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(CONNECTED_ABOVE, false));
    }

    @Override
    public @NotNull MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST  -> SHAPE_EAST;
            case WEST  -> SHAPE_WEST;
            case DOWN  -> SHAPE_DOWN;
            default    -> SHAPE_NORTH;
        };
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
        Direction face = ctx.getClickedFace();
        Direction facing;
        if (face == Direction.DOWN) {
            // Check if the block directly above is a horizontal trickle — extend it downward.
            BlockState above = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
            if (above.getBlock() instanceof WaterTrickleSourceBlock
                    && above.getValue(FACING).getAxis().isHorizontal()) {
                facing = above.getValue(FACING);
            } else {
                facing = Direction.DOWN;
            }
        } else {
            // For horizontal wall attachment, invert: clicking the north face places the block
            // to the north of it, so the spout faces south (toward the wall).
            facing = face.getAxis().isHorizontal() ? face.getOpposite() : face;
        }
        boolean connectedAbove = isTrickleAbove(ctx.getLevel(), ctx.getClickedPos(), facing);
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(CONNECTED_ABOVE, connectedAbove);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        // Recheck connected_above whenever the block above changes.
        if (direction == Direction.UP) {
            state = state.setValue(CONNECTED_ABOVE, isTrickleAbove(level, pos, state.getValue(FACING)));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CONNECTED_ABOVE);
    }

    /** Returns true if the block directly above is a trickle source facing the same wall direction. */
    private static boolean isTrickleAbove(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction facing) {
        if (!facing.getAxis().isHorizontal()) return false;
        BlockState above = level.getBlockState(pos.above());
        return above.getBlock() instanceof WaterTrickleSourceBlock
                && above.getValue(FACING) == facing;
    }
}
