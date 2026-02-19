package com.otterly76.ott.worldgen.biome;

import com.otterly76.ott.worldgen.feature.TheGardenAwakensPlacements;
import com.otterly76.ott.mixin.access.OverworldBiomesAccessor;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class TheGardenAwakensBiomes {
    public static Biome paleGarden(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawns);
        
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        OverworldBiomesAccessor.callGlobalOverworldGeneration(generation);
        
        generation.addFeature(Decoration.VEGETAL_DECORATION, TheGardenAwakensPlacements.PALE_GARDEN_VEGETATION);
        generation.addFeature(Decoration.VEGETAL_DECORATION, TheGardenAwakensPlacements.PALE_MOSS_PATCH);
        generation.addFeature(Decoration.VEGETAL_DECORATION, TheGardenAwakensPlacements.PALE_GARDEN_FLOWERS);
        
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        
        generation.addFeature(Decoration.VEGETAL_DECORATION, TheGardenAwakensPlacements.FLOWER_PALE_GARDEN);
        
        BiomeDefaultFeatures.addForestGrass(generation);
        BiomeDefaultFeatures.addDefaultExtraVegetation(generation);
        
        return (new Biome.BiomeBuilder())
                .hasPrecipitation(true)
                .temperature(0.7F)
                .downfall(0.8F)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(7768221)
                        .waterFogColor(5597568)
                        .fogColor(8484720)
                        .skyColor(12171705)
                        .grassColorOverride(7832178)
                        .foliageColorOverride(8883574)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .mobSpawnSettings(spawns.build())
                .generationSettings(generation.build())
                .build();
    }
}
