package com.otterly76.ott.block.properties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public enum VerticalLimitedConnection implements StringRepresentable {
    NONE("none", 0),
    TOP("top", 1),
    BOTTOM("bottom", 2),
    BOTH("both", 3);

    private final String name;
    private final int index;

    VerticalLimitedConnection(String name, int index) {
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

    public static EnumProperty<VerticalLimitedConnection> create(String name) {
        return EnumProperty.create(name, VerticalLimitedConnection.class);
    }
}
