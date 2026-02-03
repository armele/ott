package com.otterly76.ott.worldgen.surface.condition;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.duck.ContextAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.NotNull;

public record BiomeCondition(HolderSet<Biome> biomes) implements SurfaceRules.ConditionSource {
    public static final KeyDispatchDataCodec<BiomeCondition> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((instance) -> instance.group(OttCodecs.registrySet(Registries.BIOME, "biomes").forGetter(BiomeCondition::biomes)).apply(instance, BiomeCondition::new)));

    @Override
    public @NotNull KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.Condition apply(SurfaceRules.Context context) {
        // Use (Object) bridge to bypass the inconvertible types error
        // and call the prefixed ott$getBiome method
        return () -> this.biomes.contains(((ContextAccessor) (Object) context).ott$getBiome());
    }
}