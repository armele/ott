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
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = registerKey("pale_oak");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_OLD_GROWTH = registerKey("pale_oak_old_growth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_GNARLY = registerKey("pale_oak_gnarly");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_ANCIENT_H4 = registerKey("pale_oak_ancient_h4");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_ANCIENT_H5 = registerKey("pale_oak_ancient_h5");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_FORGOTTEN_H3 = registerKey("pale_oak_forgotten_h3");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_FORGOTTEN_H4 = registerKey("pale_oak_forgotten_h4");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_MEGA_H6 = registerKey("pale_oak_mega_h6");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_MEGA_H7 = registerKey("pale_oak_mega_h7");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_MEGA_H8 = registerKey("pale_oak_mega_h8");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = registerKey("pale_moss_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = registerKey("pale_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        context.register(PALE_OAK, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(5, 2, 1),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 3),
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.1F),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.5F, 0.3F, 0.2F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_ANCIENT_H4, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(9, 5, 3),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(7, 10),
                                ConstantInt.of(1),
                                5
                        ),
                        new TwoLayersFeatureSize(4, 0, 4)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.08F),

                                new BranchingLogsDecorator(1.0F, 4, 5, 10),

                                new BranchingLogsDecorator(1.0F, 8, 3, 7),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.70F, 0.55F, 0.30F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_ANCIENT_H5, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(9, 5, 3),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(7, 10),
                                ConstantInt.of(1),
                                6
                        ),
                        new TwoLayersFeatureSize(4, 0, 4)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.08F),

                                new BranchingLogsDecorator(1.0F, 4, 5, 10),
                                new BranchingLogsDecorator(1.0F, 8, 3, 7),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.70F, 0.55F, 0.30F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_FORGOTTEN_H3, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new ForkingTrunkPlacer(7, 3, 2),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(5, 8),
                                UniformInt.of(0, 2),
                                5
                        ),
                        new TwoLayersFeatureSize(3, 0, 3)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.04F),

                                new BranchingLogsDecorator(1.0F, 8, 3, 7),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.65F, 0.45F, 0.25F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_FORGOTTEN_H4, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new ForkingTrunkPlacer(7, 3, 2),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(5, 8),
                                UniformInt.of(0, 2),
                                6
                        ),
                        new TwoLayersFeatureSize(3, 0, 3)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.04F),

                                new BranchingLogsDecorator(1.0F, 8, 3, 7),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.65F, 0.45F, 0.25F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_OLD_GROWTH, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(7, 3, 2),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(5, 8),
                                UniformInt.of(0, 2),
                                3
                        ),
                        new TwoLayersFeatureSize(3, 0, 3)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.12F),

                                new BranchingLogsDecorator(1.0F, 5, 3, 6),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.65F, 0.45F, 0.25F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_GNARLY, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new ForkingTrunkPlacer(6, 2, 2),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(5, 8),
                                UniformInt.of(0, 2),
                                3
                        ),
                        new TwoLayersFeatureSize(3, 0, 3)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.15F),

                                new BranchingLogsDecorator(1.0F, 10, 5, 9),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.70F, 0.50F, 0.25F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_MEGA_H6, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(8, 4, 3),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(5, 8),
                                ConstantInt.of(1),
                                4
                        ),
                        new TwoLayersFeatureSize(4, 0, 4)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.05F),

                                new BranchingLogsDecorator(1.0F, 3, 5, 9),
                                new BranchingLogsDecorator(1.0F, 7, 3, 7),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.70F, 0.55F, 0.30F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_MEGA_H7, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(8, 4, 3),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(6, 9),
                                ConstantInt.of(1),
                                5
                        ),
                        new TwoLayersFeatureSize(4, 0, 4)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.05F),

                                new BranchingLogsDecorator(1.0F, 3, 5, 9),
                                new BranchingLogsDecorator(1.0F, 7, 3, 7),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.70F, 0.55F, 0.30F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_MEGA_H8, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new DarkOakTrunkPlacer(8, 4, 3),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(
                                UniformInt.of(7, 10),
                                ConstantInt.of(1),
                                6
                        ),
                        new TwoLayersFeatureSize(4, 0, 4)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.05F),

                                new BranchingLogsDecorator(1.0F, 3, 6, 10),
                                new BranchingLogsDecorator(1.0F, 7, 3, 7),

                                new TrunkTopLeavesDecorator(1.0F),
                                new PaleMossDecorator(0.70F, 0.55F, 0.30F)
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
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }
}