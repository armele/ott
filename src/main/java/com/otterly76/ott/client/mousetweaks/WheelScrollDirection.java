package com.otterly76.ott.client.mousetweaks;

public enum WheelScrollDirection {
    NORMAL, INVERTED, INVENTORY_POSITION_AWARE, INVENTORY_POSITION_AWARE_INVERTED;

    public boolean isInverted() {
        return this == INVERTED || this == INVENTORY_POSITION_AWARE_INVERTED;
    }

    public boolean isPositionAware() {
        return this == INVENTORY_POSITION_AWARE || this == INVENTORY_POSITION_AWARE_INVERTED;
    }
}
