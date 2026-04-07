package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class LitPlacedLanternBlock extends PlacedLanternBlock {

    public static final MapCodec<LitPlacedLanternBlock> CODEC = simpleCodec(LitPlacedLanternBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public LitPlacedLanternBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(ATTACH_FACE, AttachFace.FLOOR)
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false));
    }

    @Override
    public @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(ATTACH_FACE, FACING, LIT);
    }
}
