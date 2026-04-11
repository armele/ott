package com.otterly76.ott.client.mousetweaks;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

enum MouseButton {
    LEFT(0), RIGHT(1);

    private final int id;

    MouseButton(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    public static @Nullable MouseButton fromEventButton(int eventButton) {
        return switch (eventButton) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> LEFT;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> RIGHT;
            default -> null;
        };
    }
}
