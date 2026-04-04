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
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EdgeBlock extends Block implements SimpleWaterloggedBlock {

    public static final MapCodec<EdgeBlock> CODEC = simpleCodec(EdgeBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public EdgeBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
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
        double hitY = ctx.getClickLocation().y - pos.getY();
        Half half = hitY > 0.5 ? Half.TOP : Half.BOTTOM;
        FluidState fluid = ctx.getLevel().getFluidState(pos);
        BlockState state = this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(HALF, half)
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
        Half half = state.getValue(HALF);

        // Outer corners: check the block in the facing direction
        BlockState front = level.getBlockState(pos.relative(facing));
        if (front.getBlock() instanceof EdgeBlock && front.getValue(HALF) == half) {
            Direction frontFacing = front.getValue(FACING);
            if (frontFacing.getAxis() != facing.getAxis()) {
                if (isDifferentEdge(state, level, pos, frontFacing.getOpposite(), half)) {
                    return frontFacing == facing.getCounterClockWise()
                            ? StairsShape.OUTER_LEFT : StairsShape.OUTER_RIGHT;
                }
            }
        }

        // Inner corners: check the block opposite the facing direction
        BlockState back = level.getBlockState(pos.relative(facing.getOpposite()));
        if (back.getBlock() instanceof EdgeBlock && back.getValue(HALF) == half) {
            Direction backFacing = back.getValue(FACING);
            if (backFacing.getAxis() != facing.getAxis()) {
                if (isDifferentEdge(state, level, pos, backFacing, half)) {
                    return backFacing == facing.getCounterClockWise()
                            ? StairsShape.INNER_LEFT : StairsShape.INNER_RIGHT;
                }
            }
        }

        return StairsShape.STRAIGHT;
    }

    private boolean isDifferentEdge(BlockState state, BlockGetter level, BlockPos pos, Direction dir, Half half) {
        BlockState neighbor = level.getBlockState(pos.relative(dir));
        if (!(neighbor.getBlock() instanceof EdgeBlock)) return true;
        if (neighbor.getValue(HALF) != half) return true;
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
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
    }
}
