package com.otterly76.ott.worldgen.placementcondition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;

import java.util.List;

public record AnyOfPlacementCondition(List<PlacementCondition> conditions) implements PlacementCondition {
    public static final MapCodec<AnyOfPlacementCondition> CODEC;

    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        for(PlacementCondition condition : this.conditions) {
            if (condition.test(context, pos)) {
                return true;
            }
        }

        return false;
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }

    static {
        CODEC = PlacementCondition.BASE_CODEC.listOf().fieldOf("conditions").xmap(AnyOfPlacementCondition::new, AnyOfPlacementCondition::conditions);
    }
}
