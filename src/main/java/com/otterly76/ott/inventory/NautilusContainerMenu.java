package com.otterly76.ott.inventory;

import com.otterly76.ott.entity.custom.AbstractNautilusEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NautilusContainerMenu extends AbstractContainerMenu {

    private final SimpleContainer nautilusInv;
    @Nullable private final AbstractNautilusEntity nautilus;

    /** Client-side constructor (invoked by MenuType). */
    public NautilusContainerMenu(int id, @NotNull Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(2), null);
    }

    /** Server-side constructor. */
    public NautilusContainerMenu(int id, @NotNull Inventory playerInv,
                                 SimpleContainer nautilusInv, @Nullable AbstractNautilusEntity nautilus) {
        super(ModMenuTypes.NAUTILUS_INVENTORY.get(), id);
        this.nautilusInv = nautilusInv;
        this.nautilus = nautilus;

        // Slot 0: saddle
        this.addSlot(new Slot(nautilusInv, 0, 8, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.SADDLE);
            }
        });

        // Slot 1: body armor (equestrian type)
        this.addSlot(new Slot(nautilusInv, 1, 26, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof AnimalArmorItem a
                        && a.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN;
            }
        });

        // Player inventory (3 rows x 9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 102 + row * 18));
            }
        }

        // Hotbar (1 row x 9)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 160));
        }
    }

    // Slot layout: 0-1 = nautilus, 2-28 = player main inv, 29-37 = hotbar

    @Override
    public void slotsChanged(@NotNull Container container) {
        super.slotsChanged(container);
        if (container == nautilusInv && nautilus != null && nautilus.isAlive()) {
            nautilus.setItemSlot(EquipmentSlot.BODY, nautilusInv.getItem(1).copy());
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 2) {
                // Nautilus inv → player inv
                if (!this.moveItemStackTo(stack, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Player inv → appropriate nautilus slot
                if (stack.is(Items.SADDLE)) {
                    if (!this.moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (stack.getItem() instanceof AnimalArmorItem a
                        && a.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN) {
                    if (!this.moveItemStackTo(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 29) {
                    // Main inv → hotbar
                    if (!this.moveItemStackTo(stack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Hotbar → main inv
                    if (!this.moveItemStackTo(stack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }
        return result;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return nautilus == null
                || (nautilus.isAlive() && player.distanceToSqr(nautilus) < 64.0);
    }
}
