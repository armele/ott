package com.otterly76.ott.worldgen;

import com.otterly76.ott.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> VERDANT_FOREST_AZALEA = registerKey("verdant_forest_azalea");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_HOLLOW_ROOT_ARCH = registerKey("giant_hollow_root_arch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_HOLLOW_ROOT_SPIKE = registerKey("giant_hollow_root_spike");

    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_NESTED_OAK = registerKey("oak_nested_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_NESTED_OAK_TREE_FILTERED = registerKey("oak_nested_oak_tree_filtered");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        FeatureUtils.register(context, OAK_NESTED_OAK, Feature.TREE, oakNestedOak().build());
        FeatureUtils.register(context, OAK_NESTED_OAK_TREE_FILTERED, Feature.RANDOM_SELECTOR,
                new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(ModPlacedFeatures.OAK_NESTED_OAK_TREE_CHECKED), 0.5F)), placedFeatures.getOrThrow(ModPlacedFeatures.OAK_NESTED_OAK_TREE_CHECKED)));
    }

    private static TreeConfiguration.TreeConfigurationBuilder oakNestedOak() {
        return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.OAK_LOG), new StraightTrunkPlacer(4, 2, 5), BlockStateProvider.simple(Blocks.OAK_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1)).decorators(List.of(OakNestLogDecorator.INSTANCE)).ignoreVines();
    }


    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
