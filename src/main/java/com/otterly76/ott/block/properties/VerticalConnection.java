package com.otterly76.ott.block.properties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public enum VerticalConnection implements StringRepresentable {
    NONE("none", 0),
    ABOVE("above", 1),
    UNDER("under", 2),
    BOTH("both", 3);

    private final String name;
    private final int index;

    VerticalConnection(String name, int index) {
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

    public static EnumProperty<VerticalConnection> create(String name) {
        return EnumProperty.create(name, VerticalConnection.class);
    }

    public boolean hasAbove() {
        return this == ABOVE || this == BOTH;
    }

    public boolean hasUnder() {
        return this == UNDER || this == BOTH;
    }
}
