package com.otterly76.ott.worldgen.densityfunction;

import net.minecraft.world.level.levelgen.DensityFunction;

public interface MarkerFunction extends DensityFunction.SimpleFunction {
    default double compute(DensityFunction.FunctionContext context) {
        throw new IllegalStateException("Marker density function should never be computed!");
    }

    default double minValue() {
        return (double)0.0F;
    }

    default double maxValue() {
        return (double)0.0F;
    }
}