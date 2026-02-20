package com.otterly76.ott.block.shelf;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ListBackedContainer extends Container {
    NonNullList<ItemStack> getItems();

    default int count() {
        int i = 0;
        for (ItemStack itemstack : this.getItems()) {
            if (!itemstack.isEmpty()) {
                i++;
            }
        }
        return i;
    }

    @Override
    default int getContainerSize() {
        return this.getItems().size();
    }

    @Override
    default void clearContent() {
        this.getItems().clear();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack itemstack : this.getItems()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default @NotNull ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }

    @Override
    default @NotNull ItemStack removeItem(int slot, int count) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItems(), slot, count);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    default @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.getItems(), slot);
    }

    @Override
    default boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        return this.acceptsItemType(stack) && this.getItem(slot).isEmpty();
    }

    default boolean acceptsItemType(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    default void setItem(int slot, @NotNull ItemStack stack) {
        this.getItems().set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    default void setItemNoUpdate(int slot, @NotNull ItemStack stack) {
        this.getItems().set(slot, stack);
    }
}