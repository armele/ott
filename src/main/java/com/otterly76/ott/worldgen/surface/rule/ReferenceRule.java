package com.otterly76.ott.worldgen.surface.rule;


import com.otterly76.ott.api.registry.OttRegistryKeys;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ReferenceRule(HolderSet<SurfaceRules.RuleSource> rules) implements SurfaceRules.RuleSource {
    public static final KeyDispatchDataCodec<ReferenceRule> CODEC = KeyDispatchDataCodec.of(RegistryCodecs.homogeneousList(OttRegistryKeys.SURFACE_RULE).fieldOf("rules").xmap(ReferenceRule::new, ReferenceRule::rules));

    @Override
    public @NotNull KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
        if (this.rules.size() == 0) {
            return (x, y, z) -> null;
        }

        // Bridge to a wildcard list to hide the protected SurfaceRule type from the compiler during collection
        List<?> instantiatedRules = this.rules.stream()
                .map(holder -> (Object) holder.value().apply(context))
                .toList();

        if (instantiatedRules.size() == 1) {
            return (SurfaceRules.SurfaceRule) instantiatedRules.getFirst();
        }

        return (x, y, z) -> {
            for (Object ruleObj : instantiatedRules) {
                // Use a direct cast inside the lambda to call the method while bypassing visibility scope checks
                BlockState state = ((SurfaceRules.SurfaceRule) ruleObj).tryApply(x, y, z);
                if (state != null) {
                    return state;
                }
            }
            return null;
        };
    }
}

