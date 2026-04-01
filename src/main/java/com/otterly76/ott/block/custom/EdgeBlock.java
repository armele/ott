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
        BlockState right = level.getBlockState(pos.relative(facing.getClockWise()));
        BlockState left  = level.getBlockState(pos.relative(facing.getCounterClockWise()));

        boolean rightIsEdge = right.getBlock() instanceof EdgeBlock
                && right.getValue(HALF) == half
                && right.getValue(FACING) != facing.getOpposite();
        boolean leftIsEdge  = left.getBlock() instanceof EdgeBlock
                && left.getValue(HALF) == half
                && left.getValue(FACING) != facing.getOpposite();

        if (rightIsEdge) {
            Direction rightFacing = right.getValue(FACING);
            if (rightFacing == facing.getCounterClockWise()) return StairsShape.OUTER_RIGHT;
            if (rightFacing != facing) return StairsShape.INNER_RIGHT;
        }
        if (leftIsEdge) {
            Direction leftFacing = left.getValue(FACING);
            if (leftFacing == facing.getClockWise()) return StairsShape.OUTER_LEFT;
            if (leftFacing != facing) return StairsShape.INNER_LEFT;
        }
        return StairsShape.STRAIGHT;
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
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
    }
}
