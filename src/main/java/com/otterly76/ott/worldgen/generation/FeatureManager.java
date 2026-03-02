package com.otterly76.ott.worldgen.generation;

import com.otterly76.ott.worldgen.modifier.BiomeContext;
import com.otterly76.ott.worldgen.modifier.BiomeWriter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class FeatureManager {
    protected final BiomeContext context;
    protected final BiomeWriter writer;

    public FeatureManager(BiomeContext context, BiomeWriter writer) {
        this.context = context;
        this.writer = writer;
    }

    public abstract void bootstrap();

    protected Builder getOrCreateBiomeBuilder(boolean filter) {
        return new Builder(this.context, (ctx) -> filter);
    }

    protected Builder getOrCreateBiomeBuilder(ResourceKey<Biome> biome) {
        return new Builder(this.context, (ctx) -> ctx.is(biome));
    }

    protected Builder getOrCreateBiomeBuilder(TagKey<Biome> biome) {
        return new Builder(this.context, (ctx) -> ctx.is(biome));
    }

    protected Builder getOrCreateBiomeBuilder(Predicate<BiomeContext> context) {
        return new Builder(this.context, context);
    }

    protected void add(BiConsumer<BiomeContext, BiomeWriter> feature) {
        feature.accept(this.context, this.writer);
    }

    protected void addVegetation(ResourceKey<PlacedFeature> feature) {
        this.writer.addFeature(net.minecraft.world.level.levelgen.GenerationStep.Decoration.VEGETAL_DECORATION, feature);
    }

    public static class Builder {
        private final BiomeContext context;
        private final Predicate<BiomeContext> filter;

        public Builder(BiomeContext context, Predicate<BiomeContext> filter) {
            this.context = context;
            this.filter = filter;
        }

        public Builder add(Runnable runnable) {
            if (this.filter.test(this.context)) {
                runnable.run();
            }

            return this;
        }
    }
}
