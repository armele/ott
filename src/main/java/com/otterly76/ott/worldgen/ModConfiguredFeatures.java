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
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = registerKey("pale_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = registerKey("pale_moss_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PALE_GARDEN = registerKey("flower_pale_garden");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_FOREST_FLOWERS = registerKey("pale_forest_flowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = registerKey("pale_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Feature<?>> featureLookup = context.lookup(Registries.FEATURE);  // Explicit lookup for backport consistency

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

        context.register(PALE_MOSS_PATCH, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        8,
                        8,
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
                                        ),
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.matchesBlocks(BlockPos.ZERO.above(), Blocks.AIR)
                                        ),
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.not(
                                                        BlockPredicate.matchesTag(BlockPos.ZERO.above(), BlockTags.LEAVES)
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
                        6,
                        7,
                        3,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SHORT_GRASS.defaultBlockState()))
                                )),
                                List.of()
                        ))
                ) // Example vegetation
        ));

        context.register(PALE_MOSS_VEGETATION, new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        5,
                        6,
                        2,
                        Holder.direct(new PlacedFeature(
                                Holder.direct(new ConfiguredFeature<>(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.MOSS_CARPET.defaultBlockState()))
                                )),
                                List.of()
                        ))
                ) // Moss-focused
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