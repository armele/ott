package com.otterly76.ott.worldgen;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.worldgen.placement.RiverLichenFilter;
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
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH_BONEMEAL = registerKey("pale_moss_patch_bonemeal");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_CARPET_PATCH = registerKey("pale_moss_carpet_patch");

    public static final ResourceKey<PlacedFeature> PALE_OAK = registerKey("pale_oak");
    public static final ResourceKey<PlacedFeature> PALE_OAK_EXTRA = registerKey("pale_oak_extra");
    public static final ResourceKey<PlacedFeature> PALE_OAK_MEGA = registerKey("pale_oak_mega");

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

        context.register(PALE_MOSS_CARPET_PATCH, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_MOSS_CARPET_PATCH),
                commonSurfacePlacement(8)
        ));

        context.register(PALE_OAK, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK),
                List.of(
                        NoiseBasedCountPlacement.of(28, 0.35D, 12),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        paleOakGroundOnly(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_EXTRA, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK),
                List.of(
                        CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                )
        ));

        context.register(PALE_OAK_MEGA, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_MEGA),
                List.of(
                        CountPlacement.of(1),
                        RarityFilter.onAverageOnceEvery(10),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        paleOakMega2x2GroundOnly(),
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
        BlockPredicate soilBelow = BlockPredicate.matchesBlocks(
                new BlockPos(0, -1, 0),
                Blocks.GRASS_BLOCK,
                ModBlocks.PALE_MOSS_BLOCK.get()
        );

        return BlockPredicateFilter.forPredicate(soilBelow);
    }

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
            DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, "ott");

    public static final Supplier<PlacementModifierType<RiverLichenFilter>> RIVER_LICHEN_FILTER =
            PLACEMENT_MODIFIERS.register("river_lichen_filter", () -> RiverLichenFilter.TYPE);

    private static PlacementModifier paleOakMega2x2GroundOnly() {
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
                BlockPredicate.allOf(soil00, soil10, soil01, soil11)
        );
    }
}