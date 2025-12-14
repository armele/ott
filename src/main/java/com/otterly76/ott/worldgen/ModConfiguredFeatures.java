package com.otterly76.ott.worldgen;

import com.google.common.collect.ImmutableList;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = registerKey("pale_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_MEGA = registerKey("pale_oak_mega");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = registerKey("pale_moss_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = registerKey("pale_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_CARPET_PATCH = registerKey("pale_moss_carpet_patch");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        context.register(PALE_OAK, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(6, 2, 1),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.1F),
                                new ResinTrunkDecorator(0.05F),
                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleHangingMossFromLeavesDecorator(0.5F),
                                new PaleMossOnStuffDecorator(0.2F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_MEGA, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(12, 4, 2),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                        new TwoLayersFeatureSize(2, 0, 3)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.05F),
                                new ResinTrunkDecorator(0.05F),
                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleHangingMossFromLeavesDecorator(0.70F),
                                new PaleMossOnStuffDecorator(0.30F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_MOSS_PATCH, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        64,
                        6,
                        1,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get().defaultBlockState())
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.anyOf(
                                                        BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.GRASS_BLOCK),
                                                        BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.DIRT)
                                                )
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_MOSS_PATCH_BONEMEAL, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        24,
                        4,
                        1,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get().defaultBlockState())
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.anyOf(
                                                        BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.GRASS_BLOCK),
                                                        BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.DIRT)
                                                )
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_GARDEN_VEGETATION, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        24,
                        7,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                new WeightedStateProvider(
                                                        SimpleWeightedRandomList.<net.minecraft.world.level.block.state.BlockState>builder()
                                                                .add(Blocks.SHORT_GRASS.defaultBlockState(), 4)
                                                                .add(Blocks.FERN.defaultBlockState(), 1)
                                                                .add(Blocks.TALL_GRASS.defaultBlockState(), 1)
                                                                .add(Blocks.LARGE_FERN.defaultBlockState(), 1)
                                                                .build()
                                                )
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.allOf(
                                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                                        BlockPredicate.anyOf(
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), ModBlocks.PALE_MOSS_BLOCK.get()),
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.GRASS_BLOCK)
                                                        )
                                                )
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_GARDEN_FLOWERS, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        32,
                        6,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(ModBlocks.CLOSED_EYEBLOSSOM.get().defaultBlockState())
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.allOf(
                                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                                        BlockPredicate.anyOf(
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), ModBlocks.PALE_MOSS_BLOCK.get()),
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.GRASS_BLOCK)
                                                        )
                                                )
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_MOSS_VEGETATION, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        48,
                        6,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(Blocks.FERN.defaultBlockState())
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.allOf(
                                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                                        BlockPredicate.anyOf(
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), ModBlocks.PALE_MOSS_BLOCK.get()),
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.GRASS_BLOCK)
                                                        )
                                                )
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_MOSS_CARPET_PATCH, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        64,
                        6,
                        1,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState())
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.allOf(
                                                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                                        BlockPredicate.anyOf(
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), ModBlocks.PALE_MOSS_BLOCK.get()),
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.GRASS_BLOCK),
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.DIRT),
                                                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.STONE)
                                                        )
                                                )
                                        )
                                )
                        ))
                )
        ));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }
}