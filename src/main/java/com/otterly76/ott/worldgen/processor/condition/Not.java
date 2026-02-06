package com.otterly76.ott.worldgen.processor.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public record Not(ProcessorCondition condition) implements ProcessorCondition {
    public static final MapCodec<Not> CODEC;

    public boolean test(WorldGenLevel level, ProcessorCondition.Data data, StructurePlaceSettings settings, RandomSource random) {
        return !this.condition.test(level, data, settings, random);
    }

    public MapCodec<? extends ProcessorCondition> codec() {
        return CODEC;
    }

    static {
        CODEC = ProcessorCondition.BASE_CODEC.fieldOf("condition").xmap(Not::new, Not::condition);
    }
}
