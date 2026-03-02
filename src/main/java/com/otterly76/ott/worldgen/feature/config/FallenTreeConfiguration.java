package com.otterly76.ott.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.List;

public record FallenTreeConfiguration(BlockStateProvider trunkProvider, IntProvider logLength, List<TreeDecorator> stumpDecorators, List<TreeDecorator> logDecorators) implements FeatureConfiguration {
    public static final Codec<FallenTreeConfiguration> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(FallenTreeConfiguration::trunkProvider),
            IntProvider.codec(0, 16).fieldOf("log_length").forGetter(FallenTreeConfiguration::logLength),
            TreeDecorator.CODEC.listOf().fieldOf("stump_decorators").forGetter(FallenTreeConfiguration::stumpDecorators),
            TreeDecorator.CODEC.listOf().fieldOf("log_decorators").forGetter(FallenTreeConfiguration::logDecorators)
    ).apply(instance, FallenTreeConfiguration::new));

    public static class Builder {
        private final BlockStateProvider trunkProvider;
        private final IntProvider logLength;
        private List<TreeDecorator> stumpDecorators = new java.util.ArrayList<>();
        private List<TreeDecorator> logDecorators = new java.util.ArrayList<>();

        public Builder(BlockStateProvider trunkProvider, IntProvider logLength) {
            this.trunkProvider = trunkProvider;
            this.logLength = logLength;
        }

        public Builder stumpDecorators(List<TreeDecorator> decorators) {
            this.stumpDecorators = decorators;
            return this;
        }

        public Builder logDecorators(List<TreeDecorator> decorators) {
            this.logDecorators = decorators;
            return this;
        }

        public FallenTreeConfiguration build() {
            return new FallenTreeConfiguration(this.trunkProvider, this.logLength, this.stumpDecorators, this.logDecorators);
        }
    }
}
