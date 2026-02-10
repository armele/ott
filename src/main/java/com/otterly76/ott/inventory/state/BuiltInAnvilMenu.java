package com.otterly76.ott.inventory.state;

import com.otterly76.ott.mixin.common.AnvilMenuAccessor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class BuiltInAnvilMenu extends AnvilMenu implements AnvilMenuState {
    public BuiltInAnvilMenu(Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(-1, inventory, containerLevelAccess);
    }

    public void init(ItemStack leftInput, ItemStack rightInput, String itemName) {
        this.inputSlots.setItem(0, leftInput.copy());
        this.inputSlots.setItem(1, rightInput.copy());
        ((AnvilMenuAccessor) this).ott$setItemName(itemName);
        ((AnvilMenuAccessor) this).ott$getCost().set(0);
        ((AnvilMenuAccessor) this).ott$setRepairItemCountCost(0);
    }

    public final void fillResultSlots() {
        this.createResult();
    }

    public ItemStack getLeftInput() {
        return this.inputSlots.getItem(0);
    }

    public ItemStack getRightInput() {
        return this.inputSlots.getItem(1);
    }

    public ItemStack getResult() {
        return this.resultSlots.getItem(0);
    }

    public int getRepairItemCountCost() {
        return ((AnvilMenuAccessor) this).ott$getRepairItemCountCost();
    }

    public String getItemName() {
        return ((AnvilMenuAccessor) this).ott$getItemName();
    }

    public int getLevelCost() {
        return this.getCost();
    }
}
