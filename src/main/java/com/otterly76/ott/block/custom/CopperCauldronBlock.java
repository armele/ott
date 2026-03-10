package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class CopperCauldronBlock extends CauldronBlock {
    public static final MapCodec<CauldronBlock> CODEC = simpleCodec(CopperCauldronBlock::new);

    @Override
    public @NotNull MapCodec<CauldronBlock> codec() {
        return CODEC;
    }

    public CopperCauldronBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}