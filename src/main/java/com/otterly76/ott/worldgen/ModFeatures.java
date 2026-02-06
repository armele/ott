package com.otterly76.ott.worldgen;

import com.otterly76.ott.worldgen.feature.HollowRootFeature;
import com.otterly76.ott.worldgen.feature.config.HollowRootConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, "ott");

    public static final DeferredHolder<Feature<?>, SnowUnderTreesFeature> SNOW_UNDER_TREES = FEATURES.register(
            "snow_under_trees",
            () -> new SnowUnderTreesFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final DeferredHolder<Feature<?>, HollowRootFeature> HOLLOW_ROOT = FEATURES.register(
            "hollow_root",
            () -> new HollowRootFeature(HollowRootConfig.CODEC)
    );

    public static void register(IEventBus eventBus) {

        FEATURES.register(eventBus);
    }
}
