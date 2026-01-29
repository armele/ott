package com.otterly76.ott.worldgen.placement.condition;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public record OffsetPlacementCondition(PlacementCondition condition, BlockPos offset) implements PlacementCondition {
    public static final MapCodec<OffsetPlacementCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PlacementCondition.BASE_CODEC.fieldOf("condition").forGetter(OffsetPlacementCondition::condition), BlockPos.CODEC.fieldOf("offset").forGetter(OffsetPlacementCondition::offset)).apply(instance, OffsetPlacementCondition::new));

    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        return this.condition.test(context, pos.offset(this.offset));
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }
}

