package com.otterly76.ott.worldgen;

import com.google.common.collect.ImmutableList;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = registerKey("pale_oak");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_OLD_GROWTH = registerKey("pale_oak_old_growth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_TALL = registerKey("pale_oak_tall");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_GNARLY = registerKey("pale_oak_gnarly");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_MEGA = registerKey("pale_oak_mega");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = registerKey("pale_moss_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PALE_GARDEN = registerKey("flower_pale_garden");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_FOREST_FLOWERS = registerKey("pale_forest_flowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = registerKey("pale_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_BERRY_BUSHES = registerKey("pale_garden_berry_bushes");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_AZALEA = registerKey("pale_garden_azalea");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Feature<?>> featureLookup = context.lookup(Registries.FEATURE);

        context.register(PALE_OAK, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new StraightTrunkPlacer(5, 2, 1),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.1F),
                                new PaleMossDecorator(0.5F, 0.3F, 0.2F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        // Old-growth: wider canopy + chunkier silhouette
        context.register(PALE_OAK_OLD_GROWTH, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new StraightTrunkPlacer(7, 3, 2),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(ConstantInt.of(4), ConstantInt.of(1), 5),
                        new TwoLayersFeatureSize(2, 0, 2)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.12F),
                                new PaleMossDecorator(0.65F, 0.45F, 0.25F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        // Tall: higher trunk, medium canopy
        context.register(PALE_OAK_TALL, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new StraightTrunkPlacer(9, 4, 2),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 4),
                        new TwoLayersFeatureSize(2, 0, 2)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.08F),
                                new PaleMossDecorator(0.55F, 0.35F, 0.20F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        // Gnarly: forking trunk for variety
        context.register(PALE_OAK_GNARLY, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        new ForkingTrunkPlacer(6, 2, 2),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 4),
                        new TwoLayersFeatureSize(2, 0, 2)
                )
                        .decorators(ImmutableList.of(
                                new CreakingHeartDecorator(0.15F),
                                new PaleMossDecorator(0.70F, 0.50F, 0.25F)
                        ))
                        .ignoreVines()
                        .build()
        ));

        context.register(PALE_OAK_MEGA, new ConfiguredFeature<>(
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get().defaultBlockState()),
                        // Dark oak style trunk => 2x2-ish “chunk mass”
                        new DarkOakTrunkPlacer(8, 4, 3),
                        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()),
                        // Bigger canopy to sell the “wall”
                        new FancyFoliagePlacer(ConstantInt.of(4), ConstantInt.of(2), 6),
                        new TwoLayersFeatureSize(2, 0, 2)
                )
                        .decorators(ImmutableList.of(
                                // With mega trunks, hearts become more likely to have “embedded” candidates,
                                // so keep probability lower than your 1x1 trees.
                                new CreakingHeartDecorator(0.05F),
                                new PaleMossDecorator(0.70F, 0.55F, 0.30F)
                        ))
                        .ignoreVines()
                        .build()
        ));


        context.register(PALE_MOSS_PATCH, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        32,  // was 8: more tries = more successful placements
                        6,   // was 8: slightly tighter spread can make patches denser
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

        context.register(FLOWER_PALE_GARDEN, new ConfiguredFeature<>(
                Feature.FLOWER,
                new RandomPatchConfiguration(
                        24,
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
                                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.anyOf(
                                                        BlockPredicate.matchesBlocks(net.minecraft.core.BlockPos.ZERO.below(), Blocks.GRASS_BLOCK),
                                                        BlockPredicate.matchesBlocks(net.minecraft.core.BlockPos.ZERO.below(), ModBlocks.PALE_MOSS_BLOCK.get())
                                                )
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_FOREST_FLOWERS, new ConfiguredFeature<>(
                Feature.FLOWER,
                new RandomPatchConfiguration(
                        64,
                        6,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.BLUE_ORCHID.defaultBlockState()))
                                )),
                                List.of()
                        ))
                ) // Forest variant
        ));

        context.register(PALE_GARDEN_FLOWERS, new ConfiguredFeature<>(
                Feature.FLOWER,
                new RandomPatchConfiguration(
                        80,
                        6,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.POPPY.defaultBlockState()))
                                )),
                                List.of()
                        ))
                ) // Garden flowers
        ));

        context.register(PALE_GARDEN_VEGETATION, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        28, // more tries => denser ground cover
                        7,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                new WeightedStateProvider(
                                                        SimpleWeightedRandomList.<BlockState>builder()
                                                                // Heavily bias toward moss carpet
                                                                .add(Blocks.MOSS_CARPET.defaultBlockState(), 12)
                                                                // Lots of ferns
                                                                .add(Blocks.FERN.defaultBlockState(), 6)
                                                                // Some short grass (not too much)
                                                                .add(Blocks.SHORT_GRASS.defaultBlockState(), 3)
                                                                .build()
                                                )
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), ModBlocks.PALE_MOSS_BLOCK.get())
                                        )
                                )
                        ))
                )
        ));

                context.register(PALE_GARDEN_BERRY_BUSHES, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        10, // tries: tweak up/down
                        6,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(Blocks.SWEET_BERRY_BUSH.defaultBlockState())
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), ModBlocks.PALE_MOSS_BLOCK.get())
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_GARDEN_AZALEA, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        3, // azalea should be relatively rare compared to grass/ferns
                        6,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(Blocks.AZALEA.defaultBlockState())
                                        )
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), ModBlocks.PALE_MOSS_BLOCK.get())
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_MOSS_VEGETATION, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        14, // was 5: moss carpet is a big part of the “pale garden” look
                        6,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.MOSS_CARPET.defaultBlockState()))
                                )),
                                List.of(
                                        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), ModBlocks.PALE_MOSS_BLOCK.get())
                                        )
                                )
                        ))
                )
        ));

        context.register(PALE_MOSS_PATCH_BONEMEAL, new ConfiguredFeature<>(
                ModFeatures.PALE_MOSS_PATCH.get(),  // Direct reference: type-safe, no cast needed
                new RandomPatchConfiguration(
                        4,  // Tries
                        4,  // XZ spread
                        3,  // Y spread
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(
                                                BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get().defaultBlockState())  // Place moss blocks
                                        )
                                )),
                                List.of()
                        ))
                ) // Integrated: Custom feature adds hanging moss logic
        ));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("minecraft", name));  // Retain for backport
    }
}