package com.otterly76.ott.client.util;

import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ItemSlotMouseAction {
    boolean matches(Slot slot);

    boolean onMouseScrolled(double scrollDelta, int slotId, ItemStack stack);

    void onStopHovering(Slot slot);

    void onSlotClicked(Slot slot, ClickType clickType);
}
