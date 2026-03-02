package com.otterly76.ott.worldgen.placementcondition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;

public record LandBasePlacementCondition() implements PlacementCondition {
    public static final LandBasePlacementCondition INSTANCE = new LandBasePlacementCondition();
    public static final MapCodec<LandBasePlacementCondition> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        int height = context.generator().getFirstFreeHeight(pos.getX(), pos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
        return height > context.generator().getSeaLevel();
    }

    @Override
    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }
}
