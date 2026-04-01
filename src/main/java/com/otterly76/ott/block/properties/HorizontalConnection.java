package com.otterly76.ott.block.properties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public enum HorizontalConnection implements StringRepresentable {
    NONE("none", 0),
    LEFT("left", 1),
    RIGHT("right", 2),
    BOTH("both", 3);

    private final String name;
    private final int index;

    HorizontalConnection(String name, int index) {
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

    public static EnumProperty<HorizontalConnection> create(String name) {
        return EnumProperty.create(name, HorizontalConnection.class);
    }

    public boolean hasLeft() {
        return this == LEFT || this == BOTH;
    }

    public boolean hasRight() {
        return this == RIGHT || this == BOTH;
    }
}
