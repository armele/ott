package com.otterly76.ott.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    // Use "minecraft" for backport injection into vanilla registry
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, "minecraft");

    public static final DeferredHolder<Feature<?>, Feature<RandomPatchConfiguration>> PALE_MOSS_PATCH = FEATURES.register(
            "pale_moss_patch",
            () -> new PaleMossPatchFeature(RandomPatchConfiguration.CODEC)
    );

    // No bootstrap needed here since we're using DeferredRegister for direct injection
    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}