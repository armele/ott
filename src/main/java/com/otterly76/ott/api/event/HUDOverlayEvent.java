package com.otterly76.ott.api.event;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class HUDOverlayEvent extends Event implements ICancellableEvent {
    public int x;
    public int y;
    public GuiGraphics guiGraphics;

    public HUDOverlayEvent(int x, int y, GuiGraphics guiGraphics) {
        this.x = x;
        this.y = y;
        this.guiGraphics = guiGraphics;
    }

    public static class Exhaustion extends HUDOverlayEvent {
        public final float exhaustion;

        public Exhaustion(float exhaustion, int x, int y, GuiGraphics guiGraphics) {
            super(x, y, guiGraphics);
            this.exhaustion = exhaustion;
        }
    }
}