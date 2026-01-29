package com.otterly76.ott.worldgen.surface.rule;


import com.google.common.collect.ImmutableList;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TransientMergedRule(List<SurfaceRules.RuleSource> sequence, SurfaceRules.RuleSource original) implements SurfaceRules.RuleSource {
    public static final KeyDispatchDataCodec<SurfaceRules.RuleSource> CODEC;

    public @NotNull KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
        return CODEC;
    }

    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
        if (this.sequence.size() == 1) {
            return this.sequence.getFirst().apply(context);
        } else {
            ImmutableList.Builder<SurfaceRules.SurfaceRule> builder = ImmutableList.builder();

            for(SurfaceRules.RuleSource ruleSource : this.sequence) {
                builder.add(ruleSource.apply(context));
            }

            builder.add(this.original.apply(context));
            return (x, y, z) -> {

                for (SurfaceRules.SurfaceRule surfaceRule : builder.build()) {
                    BlockState blockstate = surfaceRule.tryApply(x, y, z);
                    if (blockstate != null) {
                        return blockstate;
                    }
                }

                return null;
            };
        }
    }

    static {
        CODEC = KeyDispatchDataCodec.of(RuleSource.CODEC.xmap((source) -> source, (source) -> {
            SurfaceRules.RuleSource var10000;
            if (source instanceof TransientMergedRule transientMerged) {
                var10000 = transientMerged.original;
            } else {
                var10000 = source;
            }

            return var10000;
        }).fieldOf("original_source"));
    }
}
