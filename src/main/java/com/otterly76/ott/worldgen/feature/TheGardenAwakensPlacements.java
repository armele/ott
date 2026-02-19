package com.otterly76.ott.worldgen.feature;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class TheGardenAwakensPlacements {
    public static final ResourceKey<PlacedFeature> PALE_OAK_CHECKED = registerKey("pale_oak_checked");
    public static final ResourceKey<PlacedFeature> PALE_OAK_CREAKING_CHECKED = registerKey("pale_oak_creaking_checked");
    public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = registerKey("flower_pale_garden");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = registerKey("pale_moss_patch");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        
        PlacementUtils.register(context, PALE_OAK_CHECKED, features.getOrThrow(TheGardenAwakensFeatures.PALE_OAK), 
                List.of(PlacementUtils.filteredByBlockSurvival(ModBlocks.PALE_OAK_SAPLING.get())));
        
        PlacementUtils.register(context, PALE_OAK_CREAKING_CHECKED, features.getOrThrow(TheGardenAwakensFeatures.PALE_OAK_CREAKING), 
                List.of(PlacementUtils.filteredByBlockSurvival(ModBlocks.PALE_OAK_SAPLING.get())));
        
        PlacementUtils.register(context, FLOWER_PALE_GARDEN, features.getOrThrow(TheGardenAwakensFeatures.FLOWER_PALE_GARDEN), 
                List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        
        PlacementUtils.register(context, PALE_GARDEN_VEGETATION, features.getOrThrow(TheGardenAwakensFeatures.PALE_GARDEN_VEGETATION), 
                List.of(CountPlacement.of(16), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome()));
        
        PlacementUtils.register(context, PALE_GARDEN_FLOWERS, features.getOrThrow(TheGardenAwakensFeatures.PALE_GARDEN_FLOWERS), 
                List.of(RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome()));
        
        PlacementUtils.register(context, PALE_MOSS_PATCH, features.getOrThrow(TheGardenAwakensFeatures.PALE_MOSS_PATCH), 
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }
}