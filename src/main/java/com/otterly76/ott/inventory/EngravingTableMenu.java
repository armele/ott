package com.otterly76.ott.inventory;

import com.otterly76.ott.engraving.EngravingPalette;
import net.minecraft.util.StringUtil;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EngravingTableMenu extends AbstractContainerMenu {

    protected final Inventory inventory;
    protected final Level level;

    private int selectedStackId;
    private ItemStack selectedStack = ItemStack.EMPTY;
    private ItemStack chosenStack = ItemStack.EMPTY;
    @Nullable private String filter;
    private final List<ItemStack> results = new ArrayList<>();

    public EngravingTableMenu(int containerId, Inventory inventory) {
        super(ModMenuTypes.ENGRAVING_TABLE_MENU.get(), containerId);
        this.inventory = inventory;
        this.level = inventory.player.level();
        addPlayerInvSlots();
    }

    private void addPlayerInvSlots() {
        // Main inventory rows (slots 9–35 in the player's container)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new InventorySlot(inventory, j + i * 9 + 9, 86 + j * 18, 167 + i * 18));
            }
        }
        // Hotbar (slots 0–8)
        for (int i = 0; i < 9; i++) {
            addSlot(new InventorySlot(inventory, i, 86 + i * 18, 167 + 58));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clicked(int slotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
        selectStack(slotId);
        super.clicked(slotId, button, clickType, player);
    }

    public void selectStack(int slotId) {
        if (slotId < 0 || slotId >= slots.size()) return;
        selectedStackId = slots.get(slotId).getContainerSlot();
        selectedStack = slots.get(slotId).getItem();
        chosenStack = selectedStack;
        updateResults(filter);
    }

    public void updateResults(@Nullable String filter) {
        results.clear();
        if (selectedStack.isEmpty()) return;
        this.filter = filter;
        for (ItemStack result : EngravingPalette.getResults(selectedStack.getItem())) {
            if (filter == null
                    || StringUtil.isBlank(filter)
                    || result.getDisplayName().getString().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT))) {
                results.add(result);
            }
        }
    }

    public void craft(ItemStack stack, boolean replaceAll) {
        if (stack.isEmpty()) return;

        boolean canCraft = false;
        for (ItemStack result : results) {
            if (ItemStack.isSameItemSameComponents(result, stack)) {
                canCraft = true;
                break;
            }
        }
        if (!canCraft) return;

        inventory.setItem(selectedStackId, stack.copyWithCount(inventory.getItem(selectedStackId).getCount()));
        if (replaceAll) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                if (ItemStack.isSameItem(inventory.getItem(i), selectedStack)) {
                    inventory.setItem(i, stack.copyWithCount(inventory.getItem(i).getCount()));
                }
            }
        }

        reset();
    }

    public void reset() {
        selectedStackId = 0;
        selectedStack = ItemStack.EMPTY;
        chosenStack = ItemStack.EMPTY;
        results.clear();
    }

    public ItemStack selectedStack() { return selectedStack; }
    public ItemStack chosenStack()   { return chosenStack; }
    public void setChosenStack(ItemStack stack) { chosenStack = stack; }
    public List<ItemStack> results()  { return results; }
    public Level level()              { return level; }
    public void setFilter(@Nullable String filter) { this.filter = filter; }

    // ── Display-only inventory slot — prevents item pickup ─────────────────────
    private static class InventorySlot extends Slot {
        public InventorySlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPickup(@NotNull Player player) {
            return false;
        }
    }
}
