package com.otterly76.ott.worldgen.modifier.util;

import com.otterly76.ott.worldgen.densityfunction.MarkerFunction;
import com.otterly76.ott.worldgen.densityfunction.MergedDensityFunction;
import com.otterly76.ott.worldgen.densityfunction.OriginalMarkerDensityFunction;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public class DensityFunctionWrapper {
    public static DensityFunction wrap(DensityFunction wrapped, DensityFunction wrapper) {
        if (wrapped instanceof MergedDensityFunction merged) {
            DensityFunction original = merged.original();
            return new MergedDensityFunction(original, wrapped, wrapper.mapAll((value) -> {
                if (isMarker(value)) {
                    return value instanceof OriginalMarkerDensityFunction ? original : wrapped;
                } else {
                    return value;
                }
            }));
        } else {
            return new MergedDensityFunction(wrapped, wrapped, wrapper.mapAll((value) -> isMarker(value) ? wrapped : value));
        }
    }

    private static boolean isMarker(DensityFunction df) {
        boolean var10000;
        if (df instanceof DensityFunctions.HolderHolder(net.minecraft.core.Holder<DensityFunction> function)) {
            return function.value() instanceof MarkerFunction;
        }

        return false;
    }
}
