package com.otterly76.ott.event;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;

public class ToolEventHandler {

    @SubscribeEvent
    public static void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
        if (!OttConfig.GENERAL.AUTO_TOOL_REPLACEMENT.get()) return;

        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ItemStack original = event.getOriginal();
        InteractionHand hand = event.getHand();

        if (hand == null) return;

        // Only auto-replace tools/items that are typically used as tools or weapons
        if (!isReplaceable(original)) return;

        Inventory inventory = player.getInventory();
        
        // Find replacement
        int replacementIndex = findReplacement(inventory, original);

        if (replacementIndex != -1) {
            ItemStack replacement = inventory.getItem(replacementIndex);
            ItemStack copy = replacement.copy();

            // Remove from old slot
            inventory.setItem(replacementIndex, ItemStack.EMPTY);

            // Put in hand
            player.setItemInHand(hand, copy);
        }
    }

    private static boolean isReplaceable(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof TieredItem || 
               item instanceof ShearsItem || 
               item instanceof BrushItem || 
               item instanceof ProjectileWeaponItem || 
               item instanceof TridentItem || 
               item instanceof FishingRodItem ||
               item instanceof MaceItem;
    }

    private static int findReplacement(Inventory inventory, ItemStack original) {
        Item targetItem = original.getItem();

        // 1. Try to find exact same item first
        for (int i = 0; i < inventory.items.size(); i++) {
            ItemStack stack = inventory.items.get(i);
            if (!stack.isEmpty() && stack.getItem() == targetItem) {
                return i;
            }
        }

        // 2. Try to find same tool class
        for (int i = 0; i < inventory.items.size(); i++) {
            ItemStack stack = inventory.items.get(i);
            if (!stack.isEmpty() && isSameToolType(targetItem, stack.getItem())) {
                return i;
            }
        }

        return -1;
    }

    private static boolean isSameToolType(Item original, Item other) {
        if (original instanceof PickaxeItem) return other instanceof PickaxeItem;
        if (original instanceof AxeItem) return other instanceof AxeItem;
        if (original instanceof ShovelItem) return other instanceof ShovelItem;
        if (original instanceof HoeItem) return other instanceof HoeItem;
        if (original instanceof SwordItem) return other instanceof SwordItem;
        if (original instanceof ShearsItem) return other instanceof ShearsItem;
        if (original instanceof BrushItem) return other instanceof BrushItem;
        if (original instanceof BowItem) return other instanceof BowItem;
        if (original instanceof CrossbowItem) return other instanceof CrossbowItem;
        if (original instanceof TridentItem) return other instanceof TridentItem;
        if (original instanceof FishingRodItem) return other instanceof FishingRodItem;
        if (original instanceof MaceItem) return other instanceof MaceItem;
        return false;
    }
}