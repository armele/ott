package com.otterly76.ott.worldgen.modifier;


import com.mojang.serialization.MapCodec;

public record NoOpModifier() implements Modifier {
    public static final MapCodec<NoOpModifier> CODEC = MapCodec.unit(NoOpModifier::new);

    public void applyModifier() {
    }

    public int priority() {
        return 0;
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}
