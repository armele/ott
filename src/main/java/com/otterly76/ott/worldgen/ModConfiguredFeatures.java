package com.otterly76.ott.worldgen;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> VERDANT_FOREST_AZALEA = registerKey("verdant_forest_azalea");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_HOLLOW_ROOT_ARCH = registerKey("giant_hollow_root_arch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_HOLLOW_ROOT_SPIKE = registerKey("giant_hollow_root_spike");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
    }


    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}