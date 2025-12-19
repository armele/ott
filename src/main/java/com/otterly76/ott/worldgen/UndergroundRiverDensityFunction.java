package com.otterly76.ott.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record UndergroundRiverDensityFunction(DensityFunction ridges, double offset, double scale) implements DensityFunction {
    public static final MapCodec<UndergroundRiverDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("ridges").forGetter(UndergroundRiverDensityFunction::ridges),
            MapCodec.unit(-0.8).forGetter(UndergroundRiverDensityFunction::offset),
            MapCodec.unit(2.0).forGetter(UndergroundRiverDensityFunction::scale)
    ).apply(instance, UndergroundRiverDensityFunction::new));

    @Override
    public double compute(@NotNull FunctionContext context) {
        double r = this.ridges.compute(context);

        // Tighter river: 0.8 is the center, drops off much faster (scale 6.0)
        double tube = 0.8 - (Math.abs(r) * 6.0);

        double y = context.blockY();
        // Sharper vertical bounds to keep it from leaking up/down
        double verticalEnvelope = (y > -35 && y < -25) ? 1.0 : -1.0;

        return Math.max(-1.0, Math.min(tube, verticalEnvelope));
    }

    @Override
    public void fillArray(double @NotNull [] array, @NotNull ContextProvider context) {
        context.fillAllDirectly(array, this);
    }

    @Override
    public @NotNull DensityFunction mapAll(@NotNull Visitor visitor) {
        return new UndergroundRiverDensityFunction(this.ridges.mapAll(visitor), offset, scale);
    }

    @Override
    public double minValue() { return -1.0; }

    @Override
    public double maxValue() { return 1.0; }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return new KeyDispatchDataCodec<>(CODEC);
    }
}