package com.otterly76.ott.worldgen;

import com.otterly76.ott.worldgen.feature.CactusFlowerFeature;
import com.otterly76.ott.worldgen.feature.FallenTreeFeature;
import com.otterly76.ott.worldgen.feature.HollowRootFeature;
import com.otterly76.ott.worldgen.feature.LeafLitterFeature;
import com.otterly76.ott.worldgen.feature.config.FallenTreeConfiguration;
import com.otterly76.ott.worldgen.feature.config.HollowRootConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, "ott");
    public static final DeferredRegister<Feature<?>> MINECRAFT_FEATURES = DeferredRegister.create(Registries.FEATURE, "minecraft");

    public static final DeferredHolder<Feature<?>, SnowUnderTreesFeature> SNOW_UNDER_TREES = FEATURES.register(
            "snow_under_trees",
            () -> new SnowUnderTreesFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final DeferredHolder<Feature<?>, HollowRootFeature> HOLLOW_ROOT = FEATURES.register(
            "hollow_root",
            () -> new HollowRootFeature(HollowRootConfig.CODEC)
    );

    public static final DeferredHolder<Feature<?>, FallenTreeFeature> FALLEN_TREE = MINECRAFT_FEATURES.register(
            "fallen_tree",
            () -> new FallenTreeFeature(FallenTreeConfiguration.CODEC)
    );

    public static final DeferredHolder<Feature<?>, LeafLitterFeature> LEAF_LITTER = MINECRAFT_FEATURES.register(
            "leaf_litter",
            () -> new LeafLitterFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final DeferredHolder<Feature<?>, CactusFlowerFeature> CACTUS_FLOWER = MINECRAFT_FEATURES.register(
            "cactus_flower",
            () -> new CactusFlowerFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final DeferredHolder<Feature<?>, com.otterly76.ott.worldgen.feature.BigLilyPadFeature> BIG_LILY_PAD = FEATURES.register(
            "big_lily_pad",
            () -> new com.otterly76.ott.worldgen.feature.BigLilyPadFeature(NoneFeatureConfiguration.CODEC)
    );

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
        MINECRAFT_FEATURES.register(eventBus);
    }
}
