package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeamBlock extends PergolaBlock {

    public static final MapCodec<BeamBlock> CODEC = simpleCodec(BeamBlock::new);
    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    public BeamBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AXIS_X, false)
                .setValue(AXIS_Y, false)
                .setValue(AXIS_Z, false)
                .setValue(BOTTOM, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull MapCodec<? extends PergolaBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockState base = super.getStateForPlacement(ctx);
        if (base == null) return null;
        return base.setValue(BOTTOM, isBeamBelow(ctx.getLevel(), ctx.getClickedPos()));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        BlockState updated = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (direction == Direction.DOWN) {
            return updated.setValue(BOTTOM, isBeamBelow(level, pos));
        }
        return updated;
    }

    private boolean isBeamBelow(BlockGetter level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return !(below.getBlock() instanceof BeamBlock) && !below.isAir();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AXIS_X, AXIS_Y, AXIS_Z, BOTTOM, WATERLOGGED);
    }
}
