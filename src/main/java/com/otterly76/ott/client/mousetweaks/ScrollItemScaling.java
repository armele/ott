package com.otterly76.ott.client.mousetweaks;

public enum ScrollItemScaling {
    PROPORTIONAL, ALWAYS_ONE;

    public double scale(double scrollDelta) {
        return this == ALWAYS_ONE ? Math.signum(scrollDelta) : scrollDelta;
    }
}
