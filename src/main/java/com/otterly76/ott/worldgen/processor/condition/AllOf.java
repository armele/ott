package com.otterly76.ott.worldgen.processor.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;

public record AllOf(List<ProcessorCondition> conditions) implements ProcessorCondition {
    public static final MapCodec<AllOf> CODEC;

    public boolean test(WorldGenLevel level, ProcessorCondition.Data data, StructurePlaceSettings settings, RandomSource random) {
        for(ProcessorCondition condition : this.conditions) {
            if (!condition.test(level, data, settings, random)) {
                return false;
            }
        }

        return true;
    }

    public MapCodec<? extends ProcessorCondition> codec() {
        return CODEC;
    }

    static {
        CODEC = ProcessorCondition.BASE_CODEC.listOf().fieldOf("conditions").xmap(AllOf::new, AllOf::conditions);
    }
}