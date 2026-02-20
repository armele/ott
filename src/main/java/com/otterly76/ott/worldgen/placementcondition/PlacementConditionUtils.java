package com.otterly76.ott.worldgen.placementcondition;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.DensityFunction;

final class PlacementConditionUtils {
    private PlacementConditionUtils() {}

    static boolean withinRange(DensityFunction df, BlockPos pos, Double minInclusive, Double maxInclusive) {
        double density = df.compute(new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ()));
        boolean min = minInclusive == null || density >= minInclusive;
        boolean max = maxInclusive == null || density <= maxInclusive;
        return min && max;
    }
}