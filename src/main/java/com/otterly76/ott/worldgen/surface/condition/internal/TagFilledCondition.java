package com.otterly76.ott.worldgen.surface.condition.internal;


import com.otterly76.ott.api.registry.OttRegistryKeys;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.NotNull;

public record TagFilledCondition(HolderSet<SurfaceRules.RuleSource> rules) implements SurfaceRules.ConditionSource {
    public static final KeyDispatchDataCodec<TagFilledCondition> CODEC;

    public @NotNull KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
        return CODEC;
    }

    public SurfaceRules.Condition apply(SurfaceRules.Context context) {
        return () -> this.rules.size() > 0;
    }

    static {
        CODEC = KeyDispatchDataCodec.of(RegistryCodecs.homogeneousList(OttRegistryKeys.SURFACE_RULE).fieldOf("tag").xmap(TagFilledCondition::new, TagFilledCondition::rules));
    }
}

