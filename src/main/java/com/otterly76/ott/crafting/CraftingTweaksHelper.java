package com.otterly76.ott.crafting;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class CraftingTweaksHelper {

    public static @Nullable CraftingTweaksGrid getGrid(AbstractContainerMenu menu, Player player) {
        if (menu instanceof CraftingMenu) {
            Container matrix = menu.slots.get(1).container;
            return new CraftingTweaksGrid(matrix, 1, 9, true);
        } else if (menu instanceof InventoryMenu) {
            Container matrix = menu.slots.get(1).container;
            return new CraftingTweaksGrid(matrix, 1, 4, false);
        }
        return null;
    }

    public static void rotateGrid(CraftingTweaksGrid grid, AbstractContainerMenu menu, boolean reverse) {
        if (!grid.rotateAllowed())
            return;

        Container craftMatrix = grid.matrix();
        int start = grid.startSlot();
        int size = grid.gridSize();
        Container matrixClone = new SimpleContainer(size);
        for (int i = 0; i < size; i++) {
            int slotIndex = menu.slots.get(start + i).getContainerSlot();
            matrixClone.setItem(i, craftMatrix.getItem(slotIndex));
        }

        for (int i = 0; i < size; i++) {
            if (i == 4)
                continue; // center slot of 3x3 stays in place
            int slotIndex = menu.slots.get(start + rotateSlotId(i, reverse)).getContainerSlot();
            craftMatrix.setItem(slotIndex, matrixClone.getItem(i));
        }

        menu.broadcastChanges();
    }

    private static int rotateSlotId(int slotId, boolean counterClockwise) {
        if (!counterClockwise) {
            return switch (slotId) {
                case 0 -> 1;
                case 1 -> 2;
                case 2 -> 5;
                case 5 -> 8;
                case 8 -> 7;
                case 7 -> 6;
                case 6 -> 3;
                case 3 -> 0;
                default -> 0;
            };
        } else {
            return switch (slotId) {
                case 0 -> 3;
                case 1 -> 0;
                case 2 -> 1;
                case 3 -> 6;
                case 5 -> 2;
                case 6 -> 7;
                case 7 -> 8;
                case 8 -> 5;
                default -> 0;
            };
        }
    }

    public static void balanceGrid(CraftingTweaksGrid grid, AbstractContainerMenu menu) {
        Container craftMatrix = grid.matrix();
        int start = grid.startSlot();
        int size = grid.gridSize();

        ArrayListMultimap<String, ItemStack> itemMap = ArrayListMultimap.create();
        Multiset<String> itemCount = HashMultiset.create();

        for (int i = start; i < start + size; i++) {
            int slotIndex = menu.slots.get(i).getContainerSlot();
            ItemStack itemStack = craftMatrix.getItem(slotIndex);
            if (!itemStack.isEmpty() && itemStack.getMaxStackSize() > 1) {
                ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
                String key = Objects.toString(registryName);
                DataComponentPatch patch = itemStack.getComponentsPatch();
                if (!patch.isEmpty()) {
                    key = key + "@" + patch;
                }
                itemMap.put(key, itemStack);
                itemCount.add(key, itemStack.getCount());
            }
        }

        for (String key : itemMap.keySet()) {
            List<ItemStack> balanceList = itemMap.get(key);
            int totalCount = itemCount.count(key);
            int countPerStack = totalCount / balanceList.size();
            int restCount = totalCount % balanceList.size();
            for (ItemStack itemStack : balanceList) {
                itemStack.setCount(countPerStack);
            }
            int idx = 0;
            while (restCount > 0) {
                ItemStack itemStack = balanceList.get(idx);
                if (itemStack.getCount() < itemStack.getMaxStackSize()) {
                    itemStack.grow(1);
                    restCount--;
                }
                idx++;
                if (idx >= balanceList.size())
                    idx = 0;
            }
        }

        menu.broadcastChanges();
    }

    public static void spreadGrid(CraftingTweaksGrid grid, AbstractContainerMenu menu) {
        Container craftMatrix = grid.matrix();
        int start = grid.startSlot();
        int size = grid.gridSize();

        while (true) {
            ItemStack biggestSlotStack = null;
            int biggestSlotSize = 1;
            for (int i = start; i < start + size; i++) {
                int slotIndex = menu.slots.get(i).getContainerSlot();
                ItemStack itemStack = craftMatrix.getItem(slotIndex);
                if (!itemStack.isEmpty() && itemStack.getCount() > biggestSlotSize) {
                    biggestSlotStack = itemStack;
                    biggestSlotSize = itemStack.getCount();
                }
            }

            if (biggestSlotStack == null)
                return;

            boolean emptyBiggestSlot = false;
            for (int i = start; i < start + size; i++) {
                int slotIndex = menu.slots.get(i).getContainerSlot();
                ItemStack itemStack = craftMatrix.getItem(slotIndex);
                if (itemStack.isEmpty()) {
                    if (biggestSlotStack.getCount() > 1) {
                        craftMatrix.setItem(slotIndex, biggestSlotStack.split(1));
                    } else {
                        emptyBiggestSlot = true;
                    }
                }
            }

            if (!emptyBiggestSlot)
                break;
        }

        balanceGrid(grid, menu);
    }

    public static void clearGrid(CraftingTweaksGrid grid, AbstractContainerMenu menu, Player player, boolean forced) {
        Container craftMatrix = grid.matrix();
        int start = grid.startSlot();
        int size = grid.gridSize();

        for (int i = start; i < start + size; i++) {
            int slotIndex = menu.slots.get(i).getContainerSlot();
            ItemStack itemStack = craftMatrix.getItem(slotIndex);
            if (!itemStack.isEmpty()) {
                ItemStack returnStack = itemStack.copy();
                player.getInventory().add(returnStack);
                craftMatrix.setItem(slotIndex, returnStack.getCount() == 0 ? ItemStack.EMPTY : returnStack);
                if (returnStack.getCount() > 0 && forced) {
                    player.drop(returnStack, false);
                    craftMatrix.setItem(slotIndex, ItemStack.EMPTY);
                }
            }
        }

        menu.broadcastChanges();
    }

    public static void transferIntoGrid(CraftingTweaksGrid grid, AbstractContainerMenu menu, Player player, Slot fromSlot) {
        Container craftMatrix = grid.matrix();
        int start = grid.startSlot();
        int size = grid.gridSize();
        ItemStack itemStack = fromSlot.getItem();

        if (itemStack.isEmpty())
            return;
        if (!fromSlot.mayPickup(player))
            return;
        if (fromSlot instanceof ResultSlot)
            return;
        if (fromSlot.container != player.getInventory())
            return;

        ItemStack oldStack = itemStack.copy();

        // Try to fill existing partial stacks of matching items
        int firstEmptySlot = -1;
        for (int i = start; i < start + size; i++) {
            int slotIndex = menu.slots.get(i).getContainerSlot();
            ItemStack craftStack = craftMatrix.getItem(slotIndex);
            if (!craftStack.isEmpty()) {
                if (ItemStack.isSameItemSameComponents(craftStack, itemStack)) {
                    int spaceLeft = Math.min(craftMatrix.getMaxStackSize(), craftStack.getMaxStackSize()) - craftStack.getCount();
                    if (spaceLeft > 0) {
                        ItemStack splitStack = itemStack.split(Math.min(spaceLeft, itemStack.getCount()));
                        craftStack.grow(splitStack.getCount());
                        if (itemStack.getCount() <= 0)
                            break;
                    }
                }
            } else if (firstEmptySlot == -1) {
                firstEmptySlot = slotIndex;
            }
        }

        // Fill first empty slot if items remain
        if (itemStack.getCount() > 0 && firstEmptySlot != -1) {
            ItemStack transferStack = itemStack.split(Math.min(itemStack.getCount(), craftMatrix.getMaxStackSize()));
            craftMatrix.setItem(firstEmptySlot, transferStack);
        }

        // Notify the source slot
        fromSlot.onQuickCraft(itemStack, oldStack);
        if (itemStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);
        else
            fromSlot.setChanged();

        if (itemStack.getCount() != oldStack.getCount())
            fromSlot.onTake(player, itemStack);
    }
}
