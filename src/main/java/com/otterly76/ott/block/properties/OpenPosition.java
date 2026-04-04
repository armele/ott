package com.otterly76.ott.block.properties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public enum OpenPosition implements StringRepresentable {
    CLOSED("closed"),
    HALF("half"),
    FULL("full");

    private final String name;

    OpenPosition(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public static EnumProperty<OpenPosition> create(String name) {
        return EnumProperty.create(name, OpenPosition.class);
    }
}
