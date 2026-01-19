package com.otterly76.ott.worldgen.biome;

import com.otterly76.ott.Constants;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.worldgen.ModPlacedFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;

public class ModBiomes {
    public static final ResourceKey<Biome> PALE_GARDEN = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("minecraft", "pale_garden"));

    public static final ResourceKey<Biome> VERDANT_FOREST = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "verdant_forest"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(PALE_GARDEN, paleGarden(context));
        context.register(VERDANT_FOREST, verdantForest(context));
    }

    public static Biome verdantForest(BootstrapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawns);
        // Add specific lush spawns here if you want (like parrots or bees)

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(
                context.lookup(Registries.PLACED_FEATURE),
                context.lookup(Registries.CONFIGURED_CARVER)
        );

        generation.addFeature(Decoration.VEGETAL_DECORATION, ModPlacedFeatures.VERDANT_FOREST_AZALEA);

        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        BiomeDefaultFeatures.addForestGrass(generation);
        BiomeDefaultFeatures.addDefaultExtraVegetation(generation);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.7F)
                .downfall(0.8F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(7907327)
                        .grassColorOverride(0x79C05A)
                        .foliageColorOverride(0x59AE30)
                        .backgroundMusic(ModSounds.NO_MUSIC.get())
                        .build())
                .mobSpawnSettings(spawns.build())
                .generationSettings(generation.build())
                .build();
    }

    public static Biome paleGarden(BootstrapContext<Biome> context) {
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(builder);
        BiomeGenerationSettings.Builder builder2 = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        builder2.addFeature(Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALE_GARDEN_VEGETATION);
        builder2.addFeature(Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALE_MOSS_PATCH);
        builder2.addFeature(Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALE_GARDEN_FLOWERS);
        BiomeDefaultFeatures.addDefaultOres(builder2);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder2);
        builder2.addFeature(Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FLOWER_PALE_GARDEN);
        BiomeDefaultFeatures.addForestGrass(builder2);
        BiomeDefaultFeatures.addDefaultExtraVegetation(builder2);
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
                        .backgroundMusic(ModSounds.NO_MUSIC.get())
                        .build())
                .mobSpawnSettings(builder.build())
                .generationSettings(builder2.build())
                .build();
    }
}