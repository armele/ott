package com.otterly76.ott.util.block;

import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;

public enum ModSkullType implements SkullBlock.Type {
    DRAGON_SKULL("dragon_skull");

    private final String name;

    ModSkullType(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
