package com.otterly76.ott.worldgen.biome;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModBiomes {
    public static final ResourceKey<Biome> PALE_GARDEN = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("minecraft", "pale_garden"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(PALE_GARDEN, TheGardenAwakensBiomes.paleGarden(features, carvers));
        // Lush Glade and Flatness biomes are loaded from JSON, but we can register their keys here if needed
        // However, if we want to include them in the MultiNoiseBiomeSourceParameterList in Java,
        // they must be present in the registry during bootstrap or referenced by key.
    }
    public static final ResourceKey<Biome> LUSH_GLADE = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lush_glade"));
    public static final ResourceKey<Biome> FLATNESS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flatness"));
    public static final ResourceKey<Biome> FLATNESS_WATER = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flatness_water"));
}
