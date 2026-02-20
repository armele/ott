package com.otterly76.ott.block.shelf;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum SideChainPart implements StringRepresentable {
    UNCONNECTED("unconnected"),
    RIGHT("right"),
    CENTER("center"),
    LEFT("left");

    private final String name;

    SideChainPart(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public boolean isConnected() {
        return this != UNCONNECTED;
    }

    public boolean isConnectionTowards(SideChainPart part) {
        return this == CENTER || this == part;
    }

    public boolean isChainEnd() {
        return this != CENTER;
    }

    public SideChainPart whenConnectedToTheRight() {
        return switch (this) {
            case UNCONNECTED, LEFT -> LEFT;
            case RIGHT, CENTER -> CENTER;
        };
    }

    public SideChainPart whenConnectedToTheLeft() {
        return switch (this) {
            case UNCONNECTED, RIGHT -> RIGHT;
            case LEFT, CENTER -> CENTER;
        };
    }

    public SideChainPart whenDisconnectedFromTheRight() {
        return switch (this) {
            case UNCONNECTED, LEFT -> UNCONNECTED;
            case RIGHT, CENTER -> RIGHT;
        };
    }

    public SideChainPart whenDisconnectedFromTheLeft() {
        return switch (this) {
            case UNCONNECTED, RIGHT -> UNCONNECTED;
            case LEFT, CENTER -> LEFT;
        };
    }
}