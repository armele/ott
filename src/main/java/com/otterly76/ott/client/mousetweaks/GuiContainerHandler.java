package com.otterly76.ott.client.mousetweaks;

import com.otterly76.ott.mixin.client.MouseTweaksContainerScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MerchantResultSlot;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;

import java.util.List;

class GuiContainerHandler implements IGuiScreenHandler {
    final Minecraft mc;
    private final AbstractContainerScreen<?> screen;
    private final MouseTweaksContainerScreenAccessor screenAccessor;

    GuiContainerHandler(AbstractContainerScreen<?> screen) {
        this.mc = Minecraft.getInstance();
        this.screen = screen;
        this.screenAccessor = (MouseTweaksContainerScreenAccessor) screen;
    }

    @Override
    public boolean isMouseTweaksDisabled() {
        return false;
    }

    @Override
    public boolean isWheelTweakDisabled() {
        return false;
    }

    @Override
    public List<Slot> getSlots() {
        return screen.getMenu().slots;
    }

    @Override
    public Slot getSlotUnderMouse(double mouseX, double mouseY) {
        return screenAccessor.ott$invokeFindSlot(mouseX, mouseY);
    }

    @Override
    public void disableRMBDraggingFunctionality() {
        screenAccessor.ott$setSkipNextRelease(true);

        if (screenAccessor.ott$getIsQuickCrafting() && screenAccessor.ott$getQuickCraftingButton() == 1) {
            screenAccessor.ott$setIsQuickCrafting(false);
        }
    }

    @Override
    public void clickSlot(Slot slot, MouseButton mouseButton, boolean shiftPressed) {
        screenAccessor.ott$invokeSlotClicked(
                slot,
                slot.index,
                mouseButton.getValue(),
                shiftPressed ? ClickType.QUICK_MOVE : ClickType.PICKUP
        );
    }

    @Override
    public boolean isCraftingOutput(Slot slot) {
        return slot instanceof ResultSlot || slot instanceof FurnaceResultSlot || slot instanceof MerchantResultSlot;
    }

    @Override
    public boolean isIgnored(Slot slot) {
        return false;
    }
}
