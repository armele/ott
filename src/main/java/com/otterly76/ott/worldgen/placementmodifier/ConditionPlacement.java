package com.otterly76.ott.worldgen.placementmodifier;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.worldgen.placementcondition.PlacementCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

public class ConditionPlacement extends PlacementFilter {
    public static final MapCodec<ConditionPlacement> CODEC;
    public static final PlacementModifierType<ConditionPlacement> TYPE;
    private final PlacementCondition condition;

    public ConditionPlacement(PlacementCondition condition) {
        this.condition = condition;
    }

    public PlacementCondition condition() {
        return this.condition;
    }

    protected boolean shouldPlace(@NotNull PlacementContext context, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos) {
        return this.condition.test(context, blockPos);
    }

    public @NotNull PlacementModifierType<?> type() {
        return TYPE;
    }

    static {
        CODEC = PlacementCondition.CODEC.fieldOf("condition").xmap(ConditionPlacement::new, ConditionPlacement::condition);
        TYPE = () -> CODEC;
    }
}
