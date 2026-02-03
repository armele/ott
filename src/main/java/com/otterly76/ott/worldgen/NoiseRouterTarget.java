package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.otterly76.ott.Ott;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public enum NoiseRouterTarget implements StringRepresentable {
    BARRIER("barrier", NoiseRouter::barrierNoise),
    FLUID_LEVEL_FLOODEDNESS("fluid_level_floodedness", NoiseRouter::fluidLevelFloodednessNoise),
    FLUID_LEVEL_SPREAD("fluid_level_spread", NoiseRouter::fluidLevelSpreadNoise),
    LAVA("lava", NoiseRouter::lavaNoise),
    TEMPERATURE("temperature", NoiseRouter::temperature),
    VEGETATION("vegetation", NoiseRouter::vegetation),
    CONTINENTS("continents", NoiseRouter::continents),
    EROSION("erosion", NoiseRouter::erosion),
    DEPTH("depth", NoiseRouter::depth),
    RIDGES("ridges", NoiseRouter::ridges),
    INITIAL_DENSITY("initial_density", Ott::getInitialDensity),
    FINAL_DENSITY("final_density", NoiseRouter::finalDensity),
    VEIN_TOGGLE("vein_toggle", NoiseRouter::veinToggle),
    VEIN_RIDGED("vein_ridged", NoiseRouter::veinRidged),
    VEIN_GAP("vein_gap", NoiseRouter::veinGap);

    public static final Codec<NoiseRouterTarget> CODEC = StringRepresentable.fromEnum(NoiseRouterTarget::values);
    private final String name;
    private final Function<NoiseRouter, DensityFunction> getter;

    NoiseRouterTarget(String name, Function<NoiseRouter, DensityFunction> getter) {
        if (name.equals("initial_density")) {
            this.name = Ott.getInitialDensityName();
        } else {
            this.name = name;
        }

        this.getter = getter;
    }

    public DensityFunction getDensityFunction(NoiseRouter router) {
        return this.getter.apply(router);
    }

    public @NotNull String getSerializedName() {
        return this.name;
    }
}