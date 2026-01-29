package com.otterly76.ott.worldgen.surface.condition;


import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.ConditionSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record AnyOfCondition(List<SurfaceRules.ConditionSource> conditions) implements SurfaceRules.ConditionSource {
    public static final KeyDispatchDataCodec<AnyOfCondition> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((instance) -> instance.group(ConditionSource.CODEC.listOf().fieldOf("conditions").forGetter(AnyOfCondition::conditions)).apply(instance, AnyOfCondition::new)));

    public @NotNull KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
        return CODEC;
    }

    public SurfaceRules.Condition apply(SurfaceRules.Context context) {
        return new Condition(this.conditions.stream().map((source) -> source.apply(context)).toList());
    }

    private record Condition(List<SurfaceRules.Condition> conditions) implements SurfaceRules.Condition {
        public boolean test() {
            for(SurfaceRules.Condition condition : this.conditions) {
                if (condition.test()) {
                    return true;
                }
            }

            return false;
        }
    }
}



