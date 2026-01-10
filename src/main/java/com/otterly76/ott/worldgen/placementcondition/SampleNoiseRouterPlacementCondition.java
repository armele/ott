package com.otterly76.ott.worldgen.placementcondition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.NoiseRouterTarget;
import com.otterly76.ott.worldgen.NoiseWiringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import java.util.Optional;

public record SampleNoiseRouterPlacementCondition(NoiseRouterTarget target, Optional<Double> minInclusive, Optional<Double> maxInclusive) implements PlacementCondition {
    public static final MapCodec<SampleNoiseRouterPlacementCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(NoiseRouterTarget.CODEC.fieldOf("target").forGetter(SampleNoiseRouterPlacementCondition::target), Codec.DOUBLE.optionalFieldOf("min_inclusive").forGetter(SampleNoiseRouterPlacementCondition::minInclusive), Codec.DOUBLE.optionalFieldOf("max_inclusive").forGetter(SampleNoiseRouterPlacementCondition::maxInclusive)).apply(instance, SampleNoiseRouterPlacementCondition::new));

    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        ChunkGenerator var4 = context.generator();
        if (!(var4 instanceof NoiseBasedChunkGenerator chunkGenerator)) {
            return false;
        } else {
            DensityFunction df = this.target().getDensityFunction(context.randomState().router()).mapAll(new NoiseWiringHelper(context, chunkGenerator.generatorSettings().value()));
            double density = df.compute(new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ()));
            boolean min = this.minInclusive.isEmpty() || density >= this.minInclusive.get();
            boolean max = this.maxInclusive.isEmpty() || density <= this.maxInclusive.get();
            return min && max;
        }
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }
}