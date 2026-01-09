package com.otterly76.ott.worldgen.densityfunction;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public record MergedDensityFunction(DensityFunction original, DensityFunction wrapped, DensityFunction full) implements DensityFunction {
    public static final KeyDispatchDataCodec<DensityFunction> CODEC;

    private static DensityFunction unwrappedOriginal(DensityFunction df) {
        DensityFunction var10000;
        if (df instanceof MergedDensityFunction merged) {
            var10000 = unwrappedOriginal(merged.original());
        } else {
            var10000 = df;
        }

        return var10000;
    }

    public double compute(DensityFunction.FunctionContext context) {
        return this.full.compute(context);
    }

    public void fillArray(double[] doubles, DensityFunction.ContextProvider contextProvider) {
        this.full.fillArray(doubles, contextProvider);
    }

    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
        return this.full.mapAll(visitor);
    }

    public double minValue() {
        return this.full.minValue();
    }

    public double maxValue() {
        return this.full.maxValue();
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }

    static {
        CODEC = KeyDispatchDataCodec.of(HOLDER_HELPER_CODEC.xmap((df) -> {
            DensityFunction var10000;
            if (df instanceof DensityFunctions.HolderHolder hh) {
                var10000 = (DensityFunction)hh.function().value();
            } else {
                var10000 = df;
            }

            return var10000;
        }, MergedDensityFunction::unwrappedOriginal).fieldOf("original"));
    }
}