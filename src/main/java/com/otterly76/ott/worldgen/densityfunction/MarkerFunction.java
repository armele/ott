package com.otterly76.ott.worldgen.densityfunction;

import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public interface MarkerFunction extends DensityFunction.SimpleFunction {
    default double compute(DensityFunction.@NotNull FunctionContext context) {
        throw new IllegalStateException("Marker density function should never be computed!");
    }

    default double minValue() {
        return 0.0F;
    }

    default double maxValue() {
        return 0.0F;
    }
}