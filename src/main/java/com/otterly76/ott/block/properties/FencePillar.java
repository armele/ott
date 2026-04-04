package com.otterly76.ott.block.properties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public enum FencePillar implements StringRepresentable {
    NONE("none", 0),
    PILLAR_SMALL("pillar_small", 1),
    PILLAR_BIG("pillar_big", 2),
    CAP_PILLAR_BIG("cap_pillar_big", 3);

    private final String name;
    private final int index;

    FencePillar(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public static EnumProperty<FencePillar> create(String name) {
        return EnumProperty.create(name, FencePillar.class);
    }
}
