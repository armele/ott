package com.otterly76.ott.worldgen.modifier;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public class BiomeWriter {
    private final ModifiableBiomeInfo.BiomeInfo.Builder builder;

    public BiomeWriter(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        this.builder = builder;
    }

    public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
        // In NeoForge, we need the Holder<PlacedFeature>
        // Since we are in data bootstrap, we might need a different approach
    }

    public void addSpawn(MobCategory category, MobSpawnSettings.SpawnerData spawnerData) {
        this.builder.getMobSpawnSettings().addSpawn(category, spawnerData);
    }
}