package com.otterly76.ott.worldgen.placementcondition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;

public class TruePlacementCondition implements PlacementCondition {
    public static final TruePlacementCondition INSTANCE = new TruePlacementCondition();
    public static final MapCodec<TruePlacementCondition> CODEC = MapCodec.unit(() -> INSTANCE);

    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        return true;
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }
}