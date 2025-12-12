package com.otterly76.ott.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = registerKey("flower_pale_garden");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<PlacedFeature> PALE_OAK = registerKey("pale_oak");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(FLOWER_PALE_GARDEN, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.FLOWER_PALE_GARDEN),
                commonSurfacePlacement(2)
        ));

        context.register(PALE_GARDEN_FLOWERS, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_FLOWERS),
                commonSurfacePlacement(1)
        ));

        context.register(PALE_MOSS_PATCH, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_MOSS_PATCH),
                commonSurfacePlacement(10)
        ));

        context.register(PALE_GARDEN_VEGETATION, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_VEGETATION),
                commonSurfacePlacement(6)
        ));

        context.register(PALE_OAK, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK),
                List.of(
                        CountPlacement.of(8), // TEMP: crank up to verify trees generate
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }

    private static List<PlacementModifier> commonSurfacePlacement(int count) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                BiomeFilter.biome()
        );
    }
}