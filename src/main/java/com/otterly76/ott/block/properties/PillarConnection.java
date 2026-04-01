package com.otterly76.ott.block.properties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public enum PillarConnection implements StringRepresentable {
    NONE("none", 0),
    FOUR("four_pixels", 1),
    SIX("six_pixels", 2),
    EIGHT("eight_pixels", 3),
    TEN("ten_pixels", 4);

    private final String name;
    private final int index;

    PillarConnection(String name, int index) {
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

    public static EnumProperty<PillarConnection> create(String name) {
        return EnumProperty.create(name, PillarConnection.class);
    }
}
