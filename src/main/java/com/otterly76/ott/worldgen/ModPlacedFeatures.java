package com.otterly76.ott.worldgen;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH_BONEMEAL = registerKey("pale_moss_patch_bonemeal");

    public static final ResourceKey<PlacedFeature> PALE_OAK = registerKey("pale_oak");
    public static final ResourceKey<PlacedFeature> PALE_OAK_OLD_GROWTH = registerKey("pale_oak_old_growth");
    public static final ResourceKey<PlacedFeature> PALE_OAK_GNARLY = registerKey("pale_oak_gnarly");

    public static final ResourceKey<PlacedFeature> PALE_OAK_ANCIENT_H4 = registerKey("pale_oak_ancient_h4");
    public static final ResourceKey<PlacedFeature> PALE_OAK_ANCIENT_H5 = registerKey("pale_oak_ancient_h5");
    public static final ResourceKey<PlacedFeature> PALE_OAK_FORGOTTEN_H3 = registerKey("pale_oak_forgotten_h3");
    public static final ResourceKey<PlacedFeature> PALE_OAK_FORGOTTEN_H4 = registerKey("pale_oak_forgotten_h4");

    public static final ResourceKey<PlacedFeature> PALE_OAK_MEGA_H6 = registerKey("pale_oak_mega_h6");
    public static final ResourceKey<PlacedFeature> PALE_OAK_MEGA_H7 = registerKey("pale_oak_mega_h7");
    public static final ResourceKey<PlacedFeature> PALE_OAK_MEGA_H8 = registerKey("pale_oak_mega_h8");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(PALE_GARDEN_FLOWERS, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_FLOWERS),
                commonSurfacePlacement(1)
        ));

        context.register(PALE_MOSS_PATCH, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_MOSS_PATCH),
                commonSurfacePlacement(16)
        ));

        context.register(PALE_GARDEN_VEGETATION, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_VEGETATION),
                commonSurfacePlacement(4)
        ));

        // ------------------------------------------------------------
        // DENSE FOREST BALANCE (goal: dense, but every type shows up)
        //
        // Big trees: 1 attempt when chosen + rarity gate
        // Small trees: multiple attempts per chunk for density
        // ------------------------------------------------------------

        context.register(PALE_OAK_ANCIENT_H4, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_ANCIENT_H4),
                List.of(
                        CountPlacement.of(1),
                        RarityFilter.onAverageOnceEvery(4),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakMega2x2GroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_ANCIENT_H5, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_ANCIENT_H5),
                List.of(
                        CountPlacement.of(1),
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakMega2x2GroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_MEGA_H6, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_MEGA_H6),
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakMega2x2GroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_MEGA_H7, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_MEGA_H7),
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakMega2x2GroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_MEGA_H8, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_MEGA_H8),
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(3),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakMega2x2GroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_FORGOTTEN_H3, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_FORGOTTEN_H3),
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakGroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_FORGOTTEN_H4, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_FORGOTTEN_H4),
                List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(3),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakGroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_OLD_GROWTH, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_OLD_GROWTH),
                List.of(
                        CountPlacement.of(1),
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakGroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_GNARLY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_GNARLY),
                List.of(
                        CountPlacement.of(1),
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakGroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK),
                List.of(
                        CountPlacement.of(2),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        paleOakGroundOnly(),
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

    private static PlacementModifier paleOakGroundOnly() {
        BlockPredicate airAtPos = BlockPredicate.ONLY_IN_AIR_PREDICATE;

        BlockPredicate soilBelow = BlockPredicate.matchesBlocks(
                new BlockPos(0, -1, 0),
                Blocks.GRASS_BLOCK,
                ModBlocks.PALE_MOSS_BLOCK.get()
        );

        return BlockPredicateFilter.forPredicate(BlockPredicate.allOf(airAtPos, soilBelow));
    }

    private static PlacementModifier paleOakMega2x2GroundOnly() {
        BlockPredicate air00 = BlockPredicate.matchesBlocks(
                BlockPos.ZERO,
                Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR
        );

        BlockPredicate air10 = BlockPredicate.matchesBlocks(
                new BlockPos(1, 0, 0),
                Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR
        );

        BlockPredicate air01 = BlockPredicate.matchesBlocks(
                new BlockPos(0, 0, 1),
                Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR
        );

        BlockPredicate air11 = BlockPredicate.matchesBlocks(
                new BlockPos(1, 0, 1),
                Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR
        );

        BlockPredicate soil00 = BlockPredicate.matchesBlocks(
                new BlockPos(0, -1, 0),
                Blocks.GRASS_BLOCK,
                ModBlocks.PALE_MOSS_BLOCK.get()
        );

        BlockPredicate soil10 = BlockPredicate.matchesBlocks(
                new BlockPos(1, -1, 0),
                Blocks.GRASS_BLOCK,
                ModBlocks.PALE_MOSS_BLOCK.get()
        );

        BlockPredicate soil01 = BlockPredicate.matchesBlocks(
                new BlockPos(0, -1, 1),
                Blocks.GRASS_BLOCK,
                ModBlocks.PALE_MOSS_BLOCK.get()
        );

        BlockPredicate soil11 = BlockPredicate.matchesBlocks(
                new BlockPos(1, -1, 1),
                Blocks.GRASS_BLOCK,
                ModBlocks.PALE_MOSS_BLOCK.get()
        );

        return BlockPredicateFilter.forPredicate(
                BlockPredicate.allOf(air00, air10, air01, air11, soil00, soil10, soil01, soil11)
        );
    }
}