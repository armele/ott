package com.otterly76.ott.inventory;

import com.otterly76.ott.mixin.common.AbstractContainerMenuInvoker;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.NonInteractiveResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

public final class ContainerMenuHelper {
    private ContainerMenuHelper() {
    }

    public static void setSelectedSlotLocked(AbstractContainerMenu containerMenu) {
        for(int i = 0; i < containerMenu.slots.size(); ++i) {
            Slot slot = containerMenu.slots.get(i);
            Container var4 = slot.container;
            if (var4 instanceof Inventory inventory) {
                if (inventory.selected == slot.getContainerSlot()) {
                    NonInteractiveResultSlot newSlot = new NonInteractiveResultSlot(slot.container, slot.getContainerSlot(), slot.x, slot.y) {
                        public boolean isFake() {
                            return false;
                        }
                    };
                    newSlot.index = slot.index;
                    containerMenu.slots.set(i, newSlot);
                    break;
                }
            }
        }

    }

    public static void addInventorySlots(AbstractContainerMenu containerMenu, Inventory inventory, int offsetY) {
        addInventorySlots(containerMenu, inventory, 8, offsetY);
    }

    public static void addInventorySlots(AbstractContainerMenu containerMenu, Inventory inventory, int offsetX, int offsetY) {
        int slotSize = 18;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                ((AbstractContainerMenuInvoker) containerMenu).callAddSlot(new Slot(inventory, j + i * 9 + 9, offsetX + j * 18, offsetY));
            }

            offsetY += 18;
        }

        offsetY += 4;

        for(int i = 0; i < 9; ++i) {
            ((AbstractContainerMenuInvoker) containerMenu).callAddSlot(new Slot(inventory, i, offsetX + i * 18, offsetY));
        }

    }

    public static SimpleContainer createListBackedContainer(NonNullList<ItemStack> items, @Nullable Container listener) {
        return createListBackedContainer(items, listener != null ? ($) -> listener.setChanged() : null);
    }

    public static SimpleContainer createListBackedContainer(NonNullList<ItemStack> items, @Nullable ContainerListener listener) {
        SimpleContainer simpleContainer = new SimpleContainer(items.size());
        for (int i = 0; i < items.size(); i++) {
            simpleContainer.setItem(i, items.get(i));
        }
        if (listener != null) {
            simpleContainer.addListener(listener);
        }

        return simpleContainer;
    }

    public static void copyItemsIntoContainer(NonNullList<ItemStack> from, Container to) {
        for(int i = 0; i < from.size(); ++i) {
            if (i < to.getContainerSize()) {
                to.setItem(i, from.get(i));
            }
        }

    }

    public static void copyItemsIntoList(NonNullList<ItemStack> from, NonNullList<ItemStack> to) {
        for(int i = 0; i < from.size(); ++i) {
            if (i < to.size()) {
                to.set(i, from.get(i));
            }
        }

    }

    public record EnchantmentCompatibilityResult(boolean compatible, int costAddition) {
    }

    public static EnchantmentCompatibilityResult checkEnchantmentCompatibility(Holder<Enchantment> enchantment, ItemStack stack, Iterable<Holder<Enchantment>> existingEnchantments, boolean instabuild) {
        boolean compatible = instabuild || stack.supportsEnchantment(enchantment);
        int costAddition = 0;
        for (Holder<Enchantment> existing : existingEnchantments) {
            if (!existing.equals(enchantment) && !Enchantment.areCompatible(enchantment, existing)) {
                compatible = false;
                costAddition++;
            }
        }
        return new EnchantmentCompatibilityResult(compatible, costAddition);
    }
}