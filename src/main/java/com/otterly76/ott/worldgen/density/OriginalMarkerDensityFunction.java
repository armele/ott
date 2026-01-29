package com.otterly76.ott.worldgen.density;


import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public class OriginalMarkerDensityFunction implements MarkerFunction {
    public static final KeyDispatchDataCodec<OriginalMarkerDensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new OriginalMarkerDensityFunction()));

    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}




