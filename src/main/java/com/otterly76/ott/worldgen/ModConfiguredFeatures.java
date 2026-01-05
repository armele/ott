package com.otterly76.ott.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = registerKey("pale_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = registerKey("pale_moss_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PALE_GARDEN = registerKey("flower_pale_garden");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_FOREST_FLOWERS = registerKey("pale_forest_flowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = registerKey("pale_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Empty: Data is handled by manual JSON files in resources
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.withDefaultNamespace(name));
    }
}