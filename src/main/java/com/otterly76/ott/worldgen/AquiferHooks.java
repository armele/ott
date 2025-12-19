package com.otterly76.ott.worldgen;

import net.minecraft.world.level.levelgen.DensityFunction;

public class AquiferHooks {
    // This stores our underground river math for the current worldgen thread
    public static final ThreadLocal<DensityFunction> UNDERGROUND_RIVER_FUNCTION = new ThreadLocal<>();
}