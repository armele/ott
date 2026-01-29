package com.otterly76.ott.worldgen.placement.condition;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.InclusiveRange;

import java.util.List;

public record MultipleOfPlacementCondition(List<PlacementCondition> conditions, InclusiveRange<Integer> allowedCount) implements PlacementCondition {
    public static final MapCodec<MultipleOfPlacementCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PlacementCondition.BASE_CODEC.listOf().fieldOf("conditions").forGetter(MultipleOfPlacementCondition::conditions), OttCodecs.INT_RANGE.fieldOf("allowed_count").forGetter(MultipleOfPlacementCondition::allowedCount)).apply(instance, MultipleOfPlacementCondition::new));

    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        int count = 0;

        for(PlacementCondition condition : this.conditions) {
            if (condition.test(context, pos)) {
                ++count;
                if (this.allowedCount.maxInclusive() < count) {
                    return false;
                }
            }
        }

        return this.allowedCount.isValueInRange(count);
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }
}

