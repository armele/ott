package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WaterMosaicRecessBlock extends StairBlock {

    public static final MapCodec<WaterMosaicRecessBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BlockState.CODEC.fieldOf("base_state").forGetter(b -> b.baseState),
                    propertiesCodec()
            ).apply(instance, WaterMosaicRecessBlock::new)
    );

    public WaterMosaicRecessBlock(@NotNull BlockState baseState, BlockBehaviour.@NotNull Properties properties) {
        super(baseState, properties);
    }

    @Override
    public @NotNull MapCodec<? extends StairBlock> codec() {
        return CODEC;
    }
}