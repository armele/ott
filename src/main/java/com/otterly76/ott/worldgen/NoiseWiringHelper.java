package com.otterly76.ott.worldgen;


import com.otterly76.ott.mixin.common.RandomStateAccessor;
import com.otterly76.ott.worldgen.placement.condition.PlacementCondition;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class NoiseWiringHelper implements DensityFunction.Visitor {
    private final Map<DensityFunction, DensityFunction> wrapped;
    private final boolean useLegacySource;
    private final long seed;
    final RandomState randomState;
    final PositionalRandomFactory random;

    public NoiseWiringHelper(PlacementCondition.Context context, NoiseGeneratorSettings settings) {
        this(context.seed(), settings.useLegacyRandomSource(), context.randomState(), ((RandomStateAccessor) (Object) context.randomState()).getRandom());
    }

    public NoiseWiringHelper(long seed, boolean useLegacySource, RandomState randomState, PositionalRandomFactory random) {
        this.wrapped = new HashMap<>();
        this.seed = seed;
        this.useLegacySource = useLegacySource;
        this.randomState = randomState;
        this.random = random;
    }

    @Override
    @NotNull
    public DensityFunction.NoiseHolder visitNoise(@NotNull DensityFunction.NoiseHolder noiseHolder) {
        Holder<NormalNoise.NoiseParameters> noiseData = noiseHolder.noiseData();
        if (this.useLegacySource) {
            if (noiseData.is(Noises.TEMPERATURE)) {
                @SuppressWarnings("deprecation")
                NormalNoise noise = NormalNoise.createLegacyNetherBiome(this.newLegacyInstance(0L), new NormalNoise.NoiseParameters(-7, 1.0, 1.0));
                return new DensityFunction.NoiseHolder(noiseData, noise);
            }

            if (noiseData.is(Noises.VEGETATION)) {
                @SuppressWarnings("deprecation")
                NormalNoise noise = NormalNoise.createLegacyNetherBiome(this.newLegacyInstance(1L), new NormalNoise.NoiseParameters(-7, 1.0, 1.0));
                return new DensityFunction.NoiseHolder(noiseData, noise);
            }

            if (noiseData.is(Noises.SHIFT)) {
                NormalNoise noise = NormalNoise.create(this.random.fromHashOf(Noises.SHIFT.location()), new NormalNoise.NoiseParameters(0, 0.0));
                return new DensityFunction.NoiseHolder(noiseData, noise);
            }
        }

        return noiseHolder;
    }

    @Override
    @NotNull
    public DensityFunction apply(@NotNull DensityFunction densityFunction) {
        return this.wrapped.computeIfAbsent(densityFunction, this::wrapNew);
    }

    private DensityFunction wrapNew(DensityFunction densityFunction) {
        if (densityFunction instanceof BlendedNoise noise) {
            RandomSource randomSource = this.useLegacySource ? this.newLegacyInstance(0L) : this.random.fromHashOf(ResourceLocation.withDefaultNamespace("terrain"));
            return noise.withNewRandom(randomSource);
        } else if (densityFunction.getClass().getSimpleName().equals("EndIslandDensityFunction")) {
            try {
                // Use the same reflection pattern used in your Mansion Mixin to bypass access checks
                return (DensityFunction) Class.forName("net.minecraft.world.level.levelgen.DensityFunctions$EndIslandDensityFunction")
                        .getDeclaredConstructor(long.class)
                        .newInstance(this.seed);
            } catch (Exception e) {
                return densityFunction;
            }
        } else {
            return densityFunction;
        }
    }

    private RandomSource newLegacyInstance(long noiseSeed) {
        return new LegacyRandomSource(this.seed + noiseSeed);
    }
}









