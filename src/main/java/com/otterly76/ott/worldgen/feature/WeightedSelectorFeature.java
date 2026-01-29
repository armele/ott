package com.otterly76.ott.worldgen.feature;


import com.otterly76.ott.worldgen.feature.config.WeightedSelectorConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class WeightedSelectorFeature extends Feature<WeightedSelectorConfig> {
    public static final WeightedSelectorFeature FEATURE = new WeightedSelectorFeature();

    public WeightedSelectorFeature() {
        super(WeightedSelectorConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<WeightedSelectorConfig> context) {
        WeightedSelectorConfig config = context.config();
        WorldGenLevel level = context.level();
        ChunkGenerator generator = context.chunkGenerator();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        com.otterly76.ott.util.weighted.WeightedList<Holder<PlacedFeature>> features =
                config.features();

        return features.getRandom(random)
                .map(placedFeatureHolder -> placedFeatureHolder.value().place(level, generator, random, origin))
                .orElse(false);
    }
}



