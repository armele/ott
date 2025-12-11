package com.otterly76.ott.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = registerKey("flower_pale_garden");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<PlacedFeature> PALE_OAK_CHECKED = registerKey("pale_oak_checked");
    public static final ResourceKey<PlacedFeature> PALE_OAK_CREAKING_CHECKED = registerKey("pale_oak_creaking_checked");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<PlacedFeature> lookup = context.lookup(Registries.PLACED_FEATURE);
        lookup.getOrThrow(FLOWER_PALE_GARDEN);
        lookup.getOrThrow(PALE_GARDEN_VEGETATION);
        lookup.getOrThrow(PALE_GARDEN_FLOWERS);
        lookup.getOrThrow(PALE_MOSS_PATCH);
        lookup.getOrThrow(PALE_OAK_CHECKED);
        lookup.getOrThrow(PALE_OAK_CREAKING_CHECKED);
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }
}