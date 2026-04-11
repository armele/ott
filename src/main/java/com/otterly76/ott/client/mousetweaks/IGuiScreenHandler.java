package com.otterly76.ott.client.mousetweaks;

import net.minecraft.world.inventory.Slot;

import java.util.List;

interface IGuiScreenHandler {
    boolean isMouseTweaksDisabled();

    boolean isWheelTweakDisabled();

    List<Slot> getSlots();

    Slot getSlotUnderMouse(double mouseX, double mouseY);

    void disableRMBDraggingFunctionality();

    void clickSlot(Slot slot, MouseButton mouseButton, boolean shiftPressed);

    boolean isCraftingOutput(Slot slot);

    boolean isIgnored(Slot slot);
}
