package com.otterly76.ott.util.item;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    public static void openItemGui(ServerPlayer player, int slotIndex) {
        if (!OttConfig.GENERAL.ENABLE_RIGHT_CLICK_OPEN.get()) return;

        ItemStack stack = getStackFromIndex(player, slotIndex);

        if (stack.isEmpty() || !stack.is(ModTags.ItemTags.INVENTORY_OPENABLE)) return;

        MenuProvider provider = null;
        if (stack.is(ModTags.ItemTags.DYEABLE_SHULKER_BOXES)) {
            provider = new ContainerItemMenuProvider(stack, slotIndex, 3, MenuType.SHULKER_BOX);
        } else if (stack.is(Items.BARREL)) {
            provider = new ContainerItemMenuProvider(stack, slotIndex, 3, MenuType.GENERIC_9x3);
        } else if (stack.is(Items.CRAFTING_TABLE)) {
            provider = new SimpleMenuProvider((id, inv, p) -> new CraftingMenu(id, inv, ContainerLevelAccess.create(p.level(), p.blockPosition())) {
                @Override public boolean stillValid(@NotNull Player p_39368_) { return true; }
            }, stack.getHoverName());
        } else if (stack.is(Items.LOOM)) {
            provider = new SimpleMenuProvider((id, inv, p) -> new LoomMenu(id, inv, ContainerLevelAccess.create(p.level(), p.blockPosition())) {
                @Override public boolean stillValid(@NotNull Player p_39368_) { return true; }
            }, stack.getHoverName());
        } else if (stack.is(Items.CARTOGRAPHY_TABLE)) {
            provider = new SimpleMenuProvider((id, inv, p) -> new CartographyTableMenu(id, inv, ContainerLevelAccess.create(p.level(), p.blockPosition())) {
                @Override public boolean stillValid(@NotNull Player p_39368_) { return true; }
            }, stack.getHoverName());
        } else if (stack.is(Items.GRINDSTONE)) {
            provider = new SimpleMenuProvider((id, inv, p) -> new GrindstoneMenu(id, inv, ContainerLevelAccess.create(p.level(), p.blockPosition())) {
                @Override public boolean stillValid(@NotNull Player p_39368_) { return true; }
            }, stack.getHoverName());
        } else if (stack.is(Items.STONECUTTER)) {
            provider = new SimpleMenuProvider((id, inv, p) -> new StonecutterMenu(id, inv, ContainerLevelAccess.create(p.level(), p.blockPosition())) {
                @Override public boolean stillValid(@NotNull Player p_39368_) { return true; }
            }, stack.getHoverName());
        } else if (stack.is(Items.SMITHING_TABLE)) {
            provider = new SimpleMenuProvider((id, inv, p) -> new SmithingMenu(id, inv, ContainerLevelAccess.create(p.level(), p.blockPosition())) {
                @Override public boolean stillValid(@NotNull Player p_39368_) { return true; }
            }, stack.getHoverName());
        } else if (stack.is(Items.ANVIL) || stack.is(Items.CHIPPED_ANVIL) || stack.is(Items.DAMAGED_ANVIL)) {
            provider = new SimpleMenuProvider((id, inv, p) -> new AnvilMenu(id, inv, ContainerLevelAccess.create(p.level(), p.blockPosition())) {
                @Override public boolean stillValid(@NotNull Player p_39368_) { return true; }
            }, stack.getHoverName());
        } else if (stack.is(Items.ENDER_CHEST)) {
            provider = new SimpleMenuProvider((id, inv, p) -> ChestMenu.threeRows(id, inv, p.getEnderChestInventory()), stack.getHoverName());
        }

        if (provider != null) {
            player.openMenu(provider);
        }
    }

    private record ContainerItemMenuProvider(ItemStack stack, int slotIndex, int rows, MenuType<?> menuType) implements MenuProvider {
        @Override
        public @NotNull Component getDisplayName() {
            return stack.getHoverName();
        }

        @Override
        public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
            ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            int size = rows * 9;
            NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
            contents.copyInto(list);
            SimpleContainer container = new SimpleContainer(list.toArray(new ItemStack[0]));

            if (menuType == MenuType.SHULKER_BOX) {
                return new ShulkerBoxMenu(id, playerInventory, container) {
                    @Override
                    public boolean stillValid(@NotNull Player p) {
                        return true;
                    }

                    @Override
                    public void removed(@NotNull Player p) {
                        super.removed(p);
                        save(p, container);
                    }
                };
            } else {
                return new ChestMenu(menuType, id, playerInventory, container, rows) {
                    @Override
                    public boolean stillValid(@NotNull Player p) {
                        return true;
                    }

                    @Override
                    public void removed(@NotNull Player p) {
                        super.removed(p);
                        save(p, container);
                    }
                };
            }
        }

        private void save(Player player, SimpleContainer container) {
            ItemStack currentStack = getStackFromIndex(player, slotIndex);

            if (!currentStack.isEmpty() && currentStack.is(stack.getItem())) {
                List<ItemStack> items = new ArrayList<>();
                for (int i = 0; i < container.getContainerSize(); i++) {
                    items.add(container.getItem(i));
                }
                currentStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
            }
        }
    }

    private static ItemStack getStackFromIndex(Player player, int slotIndex) {
        if (slotIndex >= 0) {
            return player.getInventory().getItem(slotIndex);
        } else if (slotIndex == -1) {
            return player.getMainHandItem();
        } else if (slotIndex == -2) {
            return player.getOffhandItem();
        } else {
            return ItemStack.EMPTY;
        }
    }
}
