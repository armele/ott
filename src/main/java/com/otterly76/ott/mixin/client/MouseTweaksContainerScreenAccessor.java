package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractContainerScreen.class)
public interface MouseTweaksContainerScreenAccessor {
    @Invoker("findSlot")
    Slot ott$invokeFindSlot(double x, double y);

    @Invoker("slotClicked")
    void ott$invokeSlotClicked(Slot slot, int index, int button, ClickType clickType);

    @Accessor("isQuickCrafting")
    boolean ott$getIsQuickCrafting();

    @Accessor("isQuickCrafting")
    void ott$setIsQuickCrafting(boolean value);

    @Accessor("quickCraftingButton")
    int ott$getQuickCraftingButton();

    @Accessor("skipNextRelease")
    void ott$setSkipNextRelease(boolean value);
}
