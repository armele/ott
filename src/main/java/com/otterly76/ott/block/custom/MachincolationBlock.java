package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.HorizontalConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MachincolationBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<MachincolationBlock> CODEC = simpleCodec(MachincolationBlock::new);
    public static final EnumProperty<HorizontalConnection> HORIZONTAL_CONNECTION =
            HorizontalConnection.create("horizontal_connection");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public MachincolationBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HORIZONTAL_CONNECTION, HorizontalConnection.NONE)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockGetter level = ctx.getLevel();
        Direction facing = ctx.getHorizontalDirection().getOpposite();
        FluidState fluid = level.getFluidState(pos);
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(HORIZONTAL_CONNECTION, getConnectionForFacing(level, pos, facing))
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        Direction facing = state.getValue(FACING);
        if (direction == facing.getClockWise() || direction == facing.getCounterClockWise()) {
            return state.setValue(HORIZONTAL_CONNECTION, getConnectionForFacing(level, pos, facing));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private HorizontalConnection getConnectionForFacing(BlockGetter level, BlockPos pos, Direction facing) {
        boolean hasLeft = isSameBlock(level, pos.relative(facing.getCounterClockWise()), facing);
        boolean hasRight = isSameBlock(level, pos.relative(facing.getClockWise()), facing);
        if (hasLeft && hasRight) return HorizontalConnection.BOTH;
        if (hasLeft) return HorizontalConnection.LEFT;
        if (hasRight) return HorizontalConnection.RIGHT;
        return HorizontalConnection.NONE;
    }

    private boolean isSameBlock(BlockGetter level, BlockPos pos, Direction facing) {
        BlockState neighbor = level.getBlockState(pos);
        return neighbor.getBlock() instanceof MachincolationBlock
                && neighbor.getValue(FACING) == facing;
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, HORIZONTAL_CONNECTION, WATERLOGGED);
    }
}
