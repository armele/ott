package com.otterly76.ott.worldgen;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = registerKey("minecraft", "pale_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = registerKey("minecraft", "pale_moss_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = registerKey("minecraft", "pale_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PALE_GARDEN = registerKey("minecraft", "flower_pale_garden");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_FOREST_FLOWERS = registerKey("minecraft", "pale_forest_flowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = registerKey("minecraft", "pale_garden_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = registerKey("minecraft", "pale_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_FLOWERS = registerKey("minecraft", "pale_garden_flowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VERDANT_FOREST_AZALEA = registerKey(Constants.MOD_ID, "verdant_forest_azalea");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Empty: Data is handled by manual JSON files in resources
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String namespace, String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(namespace, name));
    }
}