package com.otterly76.ott.worldgen.placementcondition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.NoiseWiringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import java.util.Optional;

public record SampleDensityPlacementCondition(Holder<DensityFunction> densityFunction, Optional<Double> minInclusive, Optional<Double> maxInclusive) implements PlacementCondition {
    public static final MapCodec<SampleDensityPlacementCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("density_function").forGetter(SampleDensityPlacementCondition::densityFunction), Codec.DOUBLE.optionalFieldOf("min_inclusive").forGetter(SampleDensityPlacementCondition::minInclusive), Codec.DOUBLE.optionalFieldOf("max_inclusive").forGetter(SampleDensityPlacementCondition::maxInclusive)).apply(instance, SampleDensityPlacementCondition::new));

    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        ChunkGenerator var4 = context.generator();
        if (!(var4 instanceof NoiseBasedChunkGenerator chunkGenerator)) {
            return false;
        } else {
            DensityFunction df = this.densityFunction.value().mapAll(new NoiseWiringHelper(context, chunkGenerator.generatorSettings().value()));
            return PlacementConditionUtils.withinRange(df, pos, this.minInclusive.orElse(null), this.maxInclusive.orElse(null));
        }
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }
}