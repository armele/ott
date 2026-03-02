package com.otterly76.ott.worldgen.feature;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.worldgen.ModFeatures;
import com.otterly76.ott.worldgen.AttachedToLogsDecorator;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import com.otterly76.ott.worldgen.feature.config.FallenTreeConfiguration;

import java.util.List;

public class SpringToLifeFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BUSH = registerKey("patch_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREFLY_BUSH = registerKey("patch_firefly_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_BIRCH_FOREST = registerKey("wildflowers_birch_forest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_MEADOW = registerKey("wildflowers_meadow");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DRY_GRASS = registerKey("patch_dry_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LEAF_LITTER = registerKey("patch_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEAF_LITTER = registerKey("leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CACTUS_FLOWER = registerKey("cactus_flower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_OAK_TREE = registerKey("fallen_oak_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH_TREE = registerKey("fallen_birch_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SUPER_BIRCH_TREE = registerKey("fallen_super_birch_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_JUNGLE_TREE = registerKey("fallen_jungle_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SPRUCE_TREE = registerKey("fallen_spruce_tree");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, PATCH_BUSH, Feature.RANDOM_PATCH, 
                new RandomPatchConfiguration(25, 5, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BUSH.get())))));
        
        FeatureUtils.register(context, PATCH_FIREFLY_BUSH, Feature.RANDOM_PATCH, 
                new RandomPatchConfiguration(20, 4, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.FIREFLY_BUSH.get())))));
        
        FeatureUtils.register(context, WILDFLOWERS_BIRCH_FOREST, Feature.FLOWER, 
                new RandomPatchConfiguration(64, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(flowerBedPatchBuilder(ModBlocks.WILDFLOWERS.get()))))));
        
        FeatureUtils.register(context, WILDFLOWERS_MEADOW, Feature.FLOWER, 
                new RandomPatchConfiguration(8, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(flowerBedPatchBuilder(ModBlocks.WILDFLOWERS.get()))))));
        
        FeatureUtils.register(context, PATCH_DRY_GRASS, Feature.RANDOM_PATCH, 
                grassPatch(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState(), 1).add(ModBlocks.TALL_DRY_GRASS.get().defaultBlockState(), 1).build())));
        
        FeatureUtils.register(context, PATCH_LEAF_LITTER, Feature.RANDOM_PATCH, 
                FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(leafLitterPatchBuilder())), BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
        
        FeatureUtils.register(context, FALLEN_OAK_TREE, ModFeatures.FALLEN_TREE.get(), createFallenOak().build());
        FeatureUtils.register(context, FALLEN_BIRCH_TREE, ModFeatures.FALLEN_TREE.get(), createFallenBirch(8).build());
        FeatureUtils.register(context, FALLEN_SUPER_BIRCH_TREE, ModFeatures.FALLEN_TREE.get(), createFallenBirch(15).build());
        FeatureUtils.register(context, FALLEN_JUNGLE_TREE, ModFeatures.FALLEN_TREE.get(), createFallenJungle().build());
        FeatureUtils.register(context, FALLEN_SPRUCE_TREE, ModFeatures.FALLEN_TREE.get(), createFallenSpruce().build());
        
        FeatureUtils.register(context, LEAF_LITTER, ModFeatures.LEAF_LITTER.get(), FeatureConfiguration.NONE);
        FeatureUtils.register(context, CACTUS_FLOWER, ModFeatures.CACTUS_FLOWER.get(), FeatureConfiguration.NONE);
    }

    private static RandomPatchConfiguration grassPatch(BlockStateProvider provider) {
        return FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(provider)));
    }

    private static SimpleWeightedRandomList.Builder<BlockState> flowerBedPatchBuilder(Block block) {
        return segmentedBlockPatchBuilder(block, 4);
    }

    private static SimpleWeightedRandomList.Builder<BlockState> leafLitterPatchBuilder() {
        return segmentedBlockPatchBuilder(ModBlocks.LEAF_LITTER.get(), 3);
    }

    private static SimpleWeightedRandomList.Builder<BlockState> segmentedBlockPatchBuilder(Block block, int max) {
        SimpleWeightedRandomList.Builder<BlockState> builder = SimpleWeightedRandomList.builder();

        for(int i = 1; i <= max; ++i) {
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                builder.add(block.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, i).setValue(PinkPetalsBlock.FACING, direction), 1);
            }
        }

        return builder;
    }

    private static FallenTreeConfiguration.Builder createFallenOak() {
        return createFallenTrees(Blocks.OAK_LOG, 4, 7).stumpDecorators(List.of(TrunkVineDecorator.INSTANCE));
    }

    private static FallenTreeConfiguration.Builder createFallenBirch(int i) {
        return createFallenTrees(Blocks.BIRCH_LOG, 5, i);
    }

    private static FallenTreeConfiguration.Builder createFallenJungle() {
        return createFallenTrees(Blocks.JUNGLE_LOG, 4, 11).stumpDecorators(List.of(TrunkVineDecorator.INSTANCE));
    }

    private static FallenTreeConfiguration.Builder createFallenSpruce() {
        return createFallenTrees(Blocks.SPRUCE_LOG, 6, 10);
    }

    private static FallenTreeConfiguration.Builder createFallenTrees(Block block, int minLength, int maxLength) {
        return (new FallenTreeConfiguration.Builder(BlockStateProvider.simple(block), UniformInt.of(minLength, maxLength))).logDecorators(List.of(new AttachedToLogsDecorator(0.1F, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.RED_MUSHROOM.defaultBlockState(), 2).add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1).build()), List.of(Direction.UP))));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }
}
