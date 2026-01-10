package com.otterly76.ott.worldgen.surface.rule;

import com.otterly76.ott.duck.ContextAccessor;
import com.otterly76.ott.registry.OttRegistryKeys;
import com.otterly76.ott.worldgen.bandlands.Bandlands;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.NotNull;

public record BandlandsRule(Holder<Bandlands> options) implements SurfaceRules.RuleSource {
    public static final KeyDispatchDataCodec<BandlandsRule> CODEC = KeyDispatchDataCodec.of(RegistryFileCodec.create(OttRegistryKeys.BANDLANDS, Bandlands.CODEC, false).fieldOf("options").xmap(BandlandsRule::new, BandlandsRule::options));

    @Override
    public @NotNull KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
        // Double-cast context via Object to bypass the compiler's visibility/inheritance check
        return (x, y, z) -> this.options.value().getBand(((ContextAccessor) (Object) context).ott$getSystem(), x, y, z);
    }
}