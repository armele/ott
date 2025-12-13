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
    public static final ResourceKey<PlacedFeature> PALE_OAK_OLD_GROWTH = registerKey("pale_oak_old_growth");
    public static final ResourceKey<PlacedFeature> PALE_OAK_TALL = registerKey("pale_oak_tall");
    public static final ResourceKey<PlacedFeature> PALE_OAK_GNARLY = registerKey("pale_oak_gnarly");
    public static final ResourceKey<PlacedFeature> PALE_OAK_MEGA = registerKey("pale_oak_mega");

    public static final ResourceKey<PlacedFeature> PALE_GARDEN_BERRY_BUSHES = registerKey("pale_garden_berry_bushes");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_AZALEA = registerKey("pale_garden_azalea");

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
                commonSurfacePlacement(8)
        ));

        context.register(PALE_GARDEN_VEGETATION, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_VEGETATION),
                commonSurfacePlacement(7)
        ));

        context.register(PALE_GARDEN_BERRY_BUSHES, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_BERRY_BUSHES),
                commonSurfacePlacement(1)
        ));

        context.register(PALE_GARDEN_AZALEA, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_AZALEA),
                commonSurfacePlacement(1)
        ));

        context.register(PALE_OAK, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK),
                List.of(
                        CountPlacement.of(2), // slightly lower base so the biome isn’t uniformly packed
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        // Old growth: main “look” + clumping (chunk-gated)
        context.register(PALE_OAK_OLD_GROWTH, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_OLD_GROWTH),
                List.of(
                        // About 1/2 of chunks get a dense “wall” batch:
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        // Tall: secondary variety (keep modest so old-growth defines the biome)
        context.register(PALE_OAK_TALL, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_TALL),
                List.of(
                        RarityFilter.onAverageOnceEvery(2), // about half the chunks
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        // Gnarly: make it actually “special”
        context.register(PALE_OAK_GNARLY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_GNARLY),
                List.of(
                        RarityFilter.onAverageOnceEvery(4), // was 3: this is much rarer
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        // Mega: 2x2-ish trunk, rare overall, but tends to appear in “wall chunks”
        context.register(PALE_OAK_MEGA, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_MEGA),
                List.of(
                        // About 1/5 of chunks attempt 1 mega.
                        // Increase frequency by lowering the number (e.g., 4) or decrease by raising it (e.g., 8).
                        RarityFilter.onAverageOnceEvery(4),
                        CountPlacement.of(1),
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