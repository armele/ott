package com.otterly76.ott.worldgen.modifier.util;

import com.otterly76.ott.worldgen.densityfunction.MarkerFunction;
import com.otterly76.ott.worldgen.densityfunction.MergedDensityFunction;
import com.otterly76.ott.worldgen.densityfunction.OriginalMarkerDensityFunction;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public class DensityFunctionWrapper {
    public static DensityFunction wrap(final DensityFunction wrapped, DensityFunction wrapper) {
        if (wrapped instanceof MergedDensityFunction merged) {
            final DensityFunction original = merged.original();
            return new MergedDensityFunction(original, wrapped, wrapper.mapAll(value -> {
                if (isMarker(value)) {
                    if (unwrapMarker(value) instanceof OriginalMarkerDensityFunction) {
                        return original;
                    }
                    return wrapped;
                }
                return value;
            }));
        }

        return new MergedDensityFunction(wrapped, wrapped, wrapper.mapAll(value -> {
            if (isMarker(value)) {
                return wrapped;
            }
            return value;
        }));
    }

    private static boolean isMarker(DensityFunction df) {
        return df instanceof MarkerFunction ||
               (df instanceof DensityFunctions.HolderHolder(var function) && function.value() instanceof MarkerFunction);
    }

    private static DensityFunction unwrapMarker(DensityFunction df) {
        if (df instanceof DensityFunctions.HolderHolder(var function)) {
            return function.value();
        }
        return df;
    }
}
