package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class LitBlock extends Block {

    public static final MapCodec<LitBlock> CODEC = simpleCodec(LitBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public LitBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    public @NotNull MapCodec<LitBlock> codec() {
        return CODEC;
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull net.minecraft.world.level.BlockGetter level, @NotNull net.minecraft.core.BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
