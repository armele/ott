package com.otterly76.ott.client.mousetweaks;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;

class GuiContainerCreativeHandler extends GuiContainerHandler {
    GuiContainerCreativeHandler(CreativeModeInventoryScreen screen) {
        super(screen);
    }

    @Override
    public boolean isIgnored(Slot slot) {
        return super.isIgnored(slot) || mc.player == null || slot.container != mc.player.getInventory();
    }
}
