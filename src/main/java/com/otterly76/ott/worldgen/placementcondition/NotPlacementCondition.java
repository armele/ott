package com.otterly76.ott.worldgen.placementcondition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;

public record NotPlacementCondition(PlacementCondition condition) implements PlacementCondition {
    public static final MapCodec<NotPlacementCondition> CODEC;

    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        return !this.condition.test(context, pos);
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }

    static {
        CODEC = PlacementCondition.BASE_CODEC.fieldOf("condition").xmap(NotPlacementCondition::new, NotPlacementCondition::condition);
    }
}