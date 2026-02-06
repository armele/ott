package com.otterly76.ott.worldgen.densityfunction;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.NotNull;

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

    public double compute(DensityFunction.@NotNull FunctionContext context) {
        return this.full.compute(context);
    }

    public void fillArray(double @NotNull [] doubles, DensityFunction.@NotNull ContextProvider contextProvider) {
        this.full.fillArray(doubles, contextProvider);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.@NotNull Visitor visitor) {
        return this.full.mapAll(visitor);
    }

    public double minValue() {
        return this.full.minValue();
    }

    public double maxValue() {
        return this.full.maxValue();
    }

    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }

    static {
        CODEC = KeyDispatchDataCodec.of(HOLDER_HELPER_CODEC.xmap((df) -> {
            DensityFunction var10000;
            if (df instanceof DensityFunctions.HolderHolder(net.minecraft.core.Holder<DensityFunction> function)) {
                var10000 = function.value();
            } else {
                var10000 = df;
            }

            return var10000;
        }, MergedDensityFunction::unwrappedOriginal).fieldOf("original"));
    }
}
