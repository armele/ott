package com.otterly76.ott.worldgen.processor.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public record True() implements ProcessorCondition {
    public static final MapCodec<True> CODEC = MapCodec.unit(True::new);

    public boolean test(WorldGenLevel level, ProcessorCondition.Data data, StructurePlaceSettings settings, RandomSource random) {
        return true;
    }

    public MapCodec<? extends ProcessorCondition> codec() {
        return CODEC;
    }
}