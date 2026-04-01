package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.PillarConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WallBlock;
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

public class SupportSlabBlock extends Block implements SimpleWaterloggedBlock {

    public static final MapCodec<SupportSlabBlock> CODEC = simpleCodec(SupportSlabBlock::new);
    public static final EnumProperty<PillarConnection> PILLAR_CONNECTION = PillarConnection.create("pillar_connection");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SupportSlabBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PILLAR_CONNECTION, PillarConnection.NONE)
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
        BlockGetter level = ctx.getLevel();
        FluidState fluid = level.getFluidState(pos);
        return this.defaultBlockState()
                .setValue(PILLAR_CONNECTION, getPillarConnection(level, pos))
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction == Direction.DOWN) {
            return state.setValue(PILLAR_CONNECTION, getPillarConnection(level, pos));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    protected PillarConnection getPillarConnection(BlockGetter level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        Block block = below.getBlock();
        if (block instanceof FenceBlock || block instanceof net.minecraft.world.level.block.IronBarsBlock) {
            return PillarConnection.FOUR;
        } else if (block instanceof WallBlock) {
            return PillarConnection.SIX;
        } else if (block instanceof SupportBeamBlock || block instanceof PergolaBlock) {
            return PillarConnection.EIGHT;
        } else if (!below.isAir() && below.isSolidRender(level, pos.below())) {
            return PillarConnection.NONE;
        }
        return PillarConnection.NONE;
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(PILLAR_CONNECTION, WATERLOGGED);
    }
}
