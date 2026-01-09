package com.otterly76.ott.worldgen.modifier;

import net.neoforged.neoforge.common.world.BiomeModifier;

public abstract class AbstractBiomeModifier implements Modifier {
    private final BiomeModifier neoforgeBiomeModifier;

    protected AbstractBiomeModifier(BiomeModifier neoforgeBiomeModifier) {
        this.neoforgeBiomeModifier = neoforgeBiomeModifier;
    }

    public BiomeModifier neoforgeBiomeModifier() {
        return this.neoforgeBiomeModifier;
    }

    public int priority() {
        return 0;
    }

    public void applyModifier() {
    }
}