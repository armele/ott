package com.otterly76.ott.worldgen.modifier;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Predicate;

public class BiomeContext {
    private final Holder<Biome> biome;

    public BiomeContext(Holder<Biome> biome) {
        this.biome = biome;
    }

    public boolean is(ResourceKey<Biome> key) {
        return this.biome.is(key);
    }

    public boolean is(TagKey<Biome> tag) {
        return this.biome.is(tag);
    }

    public boolean is(Predicate<BiomeContext> predicate) {
        return predicate.test(this);
    }

    public boolean hasFeature(ResourceKey<PlacedFeature> feature) {
        // This is tricky to check in neoforge biome modifier context without registries
        return false; 
    }
}
