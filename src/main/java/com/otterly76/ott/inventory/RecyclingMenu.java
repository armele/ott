package com.otterly76.ott.inventory;

import com.otterly76.ott.recycling.RecyclingManager;
import com.otterly76.ott.recycling.RecyclingSession;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecyclingMenu extends AbstractContainerMenu {
    @Nullable
    public final RecyclingSession session;
    private final ContainerData data;

    // Client-side constructor (no session needed; slots synced from server)
    public RecyclingMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, null, new ItemStackHandler(1), new ItemStackHandler(9), new SimpleContainerData(3));
    }

    public static @Nullable RecyclingMenu createForServer(int containerId, Inventory playerInventory, Player player) {
        RecyclingSession session = RecyclingManager.getSession(player.getUUID());
        if (session == null) return null;
        return new RecyclingMenu(containerId, playerInventory, session,
                session.getInputHandler(), session.getOutputHandler(), session.containerData);
    }

    private RecyclingMenu(int containerId, Inventory playerInventory, @Nullable RecyclingSession session,
                          ItemStackHandler inputHandler, ItemStackHandler outputHandler, ContainerData data) {
        super(ModMenuTypes.RECYCLING_MENU.get(), containerId);
        this.session = session;
        this.data = data;

        addDataSlots(data);

        // Input slot
        this.addSlot(new SlotItemHandler(inputHandler, 0, 26, 35));

        // Output slots (3×3 grid)
        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(outputHandler, i, 98 + 18 * (i % 3), 17 + (i / 3) * 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        }

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 102 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 160));
        }
    }

    public String getExpType() {
        return data.get(1) == 0 ? "Point" : "Level";
    }

    public int getExpAmount() {
        return data.get(0);
    }

    public int getStatus() {
        return data.get(2);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copy = sourceStack.copy();

        // Slots 0 = input, 1-9 = output, 10-36 = player inventory, 37-45 = hotbar
        if (index == 0) {
            // Input slot → player inventory
            if (!moveItemStackTo(sourceStack, 10, 46, true)) return ItemStack.EMPTY;
        } else if (index >= 1 && index <= 9) {
            // Output slots → player inventory
            if (!moveItemStackTo(sourceStack, 10, 46, true)) return ItemStack.EMPTY;
        } else {
            // Player inventory → input slot
            if (!moveItemStackTo(sourceStack, 0, 1, false)) return ItemStack.EMPTY;
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copy;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.isAlive();
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        if (!player.level().isClientSide() && session != null) {
            // Return input item
            ItemStack inputStack = session.getInputHandler().getStackInSlot(0);
            if (!inputStack.isEmpty()) {
                player.getInventory().placeItemBackInInventory(inputStack);
                session.getInputHandler().setStackInSlot(0, ItemStack.EMPTY);
            }
            // Return output items
            for (int i = 0; i < session.getOutputHandler().getSlots(); i++) {
                ItemStack outputStack = session.getOutputHandler().getStackInSlot(i);
                if (!outputStack.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(outputStack);
                    session.getOutputHandler().setStackInSlot(i, ItemStack.EMPTY);
                }
            }
            RecyclingManager.removeSession(player.getUUID());
        }
    }
}