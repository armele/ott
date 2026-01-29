package com.otterly76.ott.worldgen.structure.processor.condition;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public record RandomChance(float chance) implements ProcessorCondition {
    public static final MapCodec<RandomChance> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("chance").xmap(RandomChance::new, RandomChance::chance);

    public boolean test(WorldGenLevel level, ProcessorCondition.Data data, StructurePlaceSettings settings, RandomSource random) {
        return random.nextFloat() < this.chance;
    }

    public MapCodec<? extends ProcessorCondition> codec() {
        return CODEC;
    }
}

