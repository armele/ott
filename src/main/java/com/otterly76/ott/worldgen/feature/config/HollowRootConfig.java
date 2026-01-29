package com.otterly76.ott.worldgen.feature.config;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record HollowRootConfig(
        IntProvider radius,
        IntProvider height,
        IntProvider span,
        IntProvider groundDepth,
        FloatProvider thickness,
        FloatProvider noiseFrequency,
        BlockStateProvider blockProvider,
        BlockStateProvider decorationProvider,
        BlockStateProvider hangingProvider,
        Holder<ConfiguredFeature<?, ?>> surfaceFeature
) implements FeatureConfiguration {
    public static final Codec<HollowRootConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            IntProvider.POSITIVE_CODEC.fieldOf("radius").forGetter(HollowRootConfig::radius),
            IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(HollowRootConfig::height),
            IntProvider.CODEC.fieldOf("span").forGetter(HollowRootConfig::span),
            IntProvider.CODEC.fieldOf("ground_depth").forGetter(HollowRootConfig::groundDepth),
            FloatProvider.CODEC.fieldOf("thickness").forGetter(HollowRootConfig::thickness),
            FloatProvider.CODEC.fieldOf("noise_frequency").forGetter(HollowRootConfig::noiseFrequency),
            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(HollowRootConfig::blockProvider),
            BlockStateProvider.CODEC.fieldOf("decoration_provider").forGetter(HollowRootConfig::decorationProvider),
            BlockStateProvider.CODEC.fieldOf("hanging_provider").forGetter(HollowRootConfig::hangingProvider),
            ConfiguredFeature.CODEC.fieldOf("surface_feature").forGetter(HollowRootConfig::surfaceFeature)
    ).apply(instance, HollowRootConfig::new));
}
