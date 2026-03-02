package com.otterly76.ott.worldgen.feature;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class SpringToLifePlacements {
    public static final ResourceKey<PlacedFeature> PATCH_BUSH = registerKey("patch_bush");
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER = registerKey("patch_firefly_bush_near_water");
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP = registerKey("patch_firefly_bush_near_water_swamp");
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_SWAMP = registerKey("patch_firefly_bush_swamp");
    public static final ResourceKey<PlacedFeature> WILDFLOWERS_BIRCH_FOREST = registerKey("wildflowers_birch_forest");
    public static final ResourceKey<PlacedFeature> WILDFLOWERS_MEADOW = registerKey("wildflowers_meadow");
    public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_BADLANDS = registerKey("patch_dry_grass_badlands");
    public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_DESERT = registerKey("patch_dry_grass_desert");
    public static final ResourceKey<PlacedFeature> PATCH_LEAF_LITTER = registerKey("patch_leaf_litter");
    public static final ResourceKey<PlacedFeature> LEAF_LITTER = registerKey("leaf_litter");
    public static final ResourceKey<PlacedFeature> CACTUS_FLOWER = registerKey("cactus_flower");
    public static final ResourceKey<PlacedFeature> FALLEN_OAK_TREE = registerKey("fallen_oak_tree");
    public static final ResourceKey<PlacedFeature> FALLEN_BIRCH_TREE = registerKey("fallen_birch_tree");
    public static final ResourceKey<PlacedFeature> FALLEN_SUPER_BIRCH_TREE = registerKey("fallen_super_birch_tree");
    public static final ResourceKey<PlacedFeature> FALLEN_JUNGLE_TREE = registerKey("fallen_jungle_tree");
    public static final ResourceKey<PlacedFeature> FALLEN_SPRUCE_TREE = registerKey("fallen_spruce_tree");
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_OAK_TREE = registerKey("placed_fallen_oak_tree");
    public static final ResourceKey<PlacedFeature> PLACED_RARE_FALLEN_BIRCH_TREE = registerKey("placed_rare_fallen_birch_tree");
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_BIRCH_TREE = registerKey("placed_fallen_birch_tree");
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_SUPER_BIRCH_TREE = registerKey("placed_fallen_super_birch_tree");
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_JUNGLE_TREE = registerKey("placed_fallen_jungle_tree");
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_SPRUCE_TREE = registerKey("placed_fallen_spruce_tree");
    public static final ResourceKey<PlacedFeature> PLACED_RARE_FALLEN_SPRUCE_TREE = registerKey("placed_rare_fallen_spruce_tree");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> patch = features.getOrThrow(SpringToLifeFeatures.PATCH_FIREFLY_BUSH);
        
        PlacementUtils.register(context, PATCH_BUSH, features.getOrThrow(SpringToLifeFeatures.PATCH_BUSH), 
                List.of(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        
        PlacementUtils.register(context, PATCH_FIREFLY_BUSH_NEAR_WATER, patch, 
                List.of(CountPlacement.of(2), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome(), nearWaterPredicate(ModBlocks.FIREFLY_BUSH.get())));
        
        PlacementUtils.register(context, PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP, patch, 
                List.of(CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), nearWaterPredicate(ModBlocks.FIREFLY_BUSH.get())));
        
        PlacementUtils.register(context, PATCH_FIREFLY_BUSH_SWAMP, patch, 
                List.of(RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        
        PlacementUtils.register(context, WILDFLOWERS_BIRCH_FOREST, features.getOrThrow(SpringToLifeFeatures.WILDFLOWERS_BIRCH_FOREST), 
                List.of(CountPlacement.of(3), RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        
        PlacementUtils.register(context, WILDFLOWERS_MEADOW, features.getOrThrow(SpringToLifeFeatures.WILDFLOWERS_MEADOW), 
                List.of(NoiseThresholdCountPlacement.of(-0.8, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        
        PlacementUtils.register(context, PATCH_DRY_GRASS_BADLANDS, features.getOrThrow(SpringToLifeFeatures.PATCH_DRY_GRASS), 
                List.of(RarityFilter.onAverageOnceEvery(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        
        PlacementUtils.register(context, PATCH_DRY_GRASS_DESERT, features.getOrThrow(SpringToLifeFeatures.PATCH_DRY_GRASS), 
                List.of(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        
        PlacementUtils.register(context, PATCH_LEAF_LITTER, features.getOrThrow(SpringToLifeFeatures.PATCH_LEAF_LITTER), VegetationPlacements.worldSurfaceSquaredWithCount(2));
        
        PlacementUtils.register(context, FALLEN_OAK_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_OAK_TREE), 
                List.of(PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)));
        
        PlacementUtils.register(context, FALLEN_BIRCH_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_BIRCH_TREE), 
                List.of(PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)));
        
        PlacementUtils.register(context, FALLEN_SUPER_BIRCH_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_SUPER_BIRCH_TREE), 
                List.of(PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)));
        
        PlacementUtils.register(context, FALLEN_SPRUCE_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_SPRUCE_TREE), 
                List.of(PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING)));
        
        PlacementUtils.register(context, FALLEN_JUNGLE_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_JUNGLE_TREE), 
                List.of(PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING)));
        
        PlacementUtils.register(context, LEAF_LITTER, features.getOrThrow(SpringToLifeFeatures.LEAF_LITTER), 
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(16, 0.1F, 1)));
        
        PlacementUtils.register(context, CACTUS_FLOWER, features.getOrThrow(SpringToLifeFeatures.CACTUS_FLOWER), 
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(16, 0.1F, 1)));
        
        PlacementUtils.register(context, PLACED_FALLEN_OAK_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_OAK_TREE), 
                VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.OAK_SAPLING));
        
        PlacementUtils.register(context, PLACED_RARE_FALLEN_BIRCH_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_BIRCH_TREE), 
                VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(400), Blocks.BIRCH_SAPLING));
        
        PlacementUtils.register(context, PLACED_FALLEN_BIRCH_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_BIRCH_TREE), 
                VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.BIRCH_SAPLING));
        
        PlacementUtils.register(context, PLACED_FALLEN_SUPER_BIRCH_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_SUPER_BIRCH_TREE), 
                VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(160), Blocks.BIRCH_SAPLING));
        
        PlacementUtils.register(context, PLACED_FALLEN_JUNGLE_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_JUNGLE_TREE), 
                VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.JUNGLE_SAPLING));
        
        PlacementUtils.register(context, PLACED_FALLEN_SPRUCE_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_SPRUCE_TREE), 
                VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.SPRUCE_SAPLING));
        
        PlacementUtils.register(context, PLACED_RARE_FALLEN_SPRUCE_TREE, features.getOrThrow(SpringToLifeFeatures.FALLEN_SPRUCE_TREE), 
                VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(120), Blocks.SPRUCE_SAPLING));
    }

    public static BlockPredicateFilter nearWaterPredicate(Block block) {
        return BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.ONLY_IN_AIR_PREDICATE, 
                BlockPredicate.wouldSurvive(block.defaultBlockState(), BlockPos.ZERO), 
                BlockPredicate.anyOf(
                        BlockPredicate.matchesFluids(new BlockPos(1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER), 
                        BlockPredicate.matchesFluids(new BlockPos(-1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER), 
                        BlockPredicate.matchesFluids(new BlockPos(0, -1, 1), Fluids.WATER, Fluids.FLOWING_WATER), 
                        BlockPredicate.matchesFluids(new BlockPos(0, -1, -1), Fluids.WATER, Fluids.FLOWING_WATER)
                )
        ));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }
}
