package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResinClumpBlock extends MultifaceBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<ResinClumpBlock> CODEC = simpleCodec(ResinClumpBlock::new);
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    @Override
    protected @NotNull MapCodec<? extends MultifaceBlock> codec() {
        return CODEC;
    }

    public ResinClumpBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull MultifaceSpreader getSpreader() {
        return new MultifaceSpreader(new NoOpSpreadConfig());
    }

    private static class NoOpSpreadConfig implements MultifaceSpreader.SpreadConfig {
        @Override
        public @Nullable BlockState getStateForPlacement(@NotNull BlockState currentState, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction lookingDirection) {
            return null;
        }

        @Override
        public boolean canSpreadInto(@NotNull BlockGetter level, @NotNull BlockPos pos, MultifaceSpreader.@NotNull SpreadPos spreadPos) {
            return false;
        }
    }
}
