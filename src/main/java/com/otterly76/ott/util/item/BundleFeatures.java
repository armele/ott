package com.otterly76.ott.util.item;

import com.otterly76.ott.ModChecker;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.duck.IBundle;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;

import java.util.Optional;

public class BundleFeatures {
    public static boolean onBundleUpdate() {
        return OttConfig.BUNDLES.UPDATED_BUNDLES.get() && !ModChecker.BEST_BUNDLES_LOADED;
    }

    public static boolean canItemBeInBundle(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().canFitInsideContainerItems();
    }

    public static void toggleSelectedItem(ItemStack stack, int index) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents != null) {
            BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
            ((IBundle.Mutable)mutable).toggleSelectedItem(index);
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        }
    }

    public static int getSelectedItem(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return ((IBundle)(Object)contents).getSelectedItem();
    }

    public static ItemStack getSelectedItemStack(ItemStack stack) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) return ItemStack.EMPTY;
        IBundle ibundle = (IBundle)(Object)contents;
        return ibundle.getSelectedItem() != -1 ? contents.getItemUnsafe(ibundle.getSelectedItem()) : ItemStack.EMPTY;
    }

    public static int getNumberOfItemsToShow(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return ((IBundle)(Object)contents).getNumberOfItemsToShow();
    }

    public static Optional<ItemStack> removeOneItemFromBundle(ItemStack stack, Player player, BundleContents contents) {
        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
        ItemStack itemStack = mutable.removeOne();
        if (!itemStack.isEmpty()) {
            playRemoveOneSound(player);
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
            return Optional.of(itemStack);
        } else {
            return Optional.empty();
        }
    }

    public static Item getByColor(DyeColor dyeColor) {
        return switch (dyeColor) {
            case WHITE -> ModItems.BUNDLES.get("white").get();
            case ORANGE -> ModItems.BUNDLES.get("orange").get();
            case MAGENTA -> ModItems.BUNDLES.get("magenta").get();
            case LIGHT_BLUE -> ModItems.BUNDLES.get("light_blue").get();
            case YELLOW -> ModItems.BUNDLES.get("yellow").get();
            case LIME -> ModItems.BUNDLES.get("lime").get();
            case PINK -> ModItems.BUNDLES.get("pink").get();
            case GRAY -> ModItems.BUNDLES.get("gray").get();
            case LIGHT_GRAY -> ModItems.BUNDLES.get("light_gray").get();
            case CYAN -> ModItems.BUNDLES.get("cyan").get();
            case BLUE -> ModItems.BUNDLES.get("blue").get();
            case BROWN -> ModItems.BUNDLES.get("brown").get();
            case GREEN -> ModItems.BUNDLES.get("green").get();
            case RED -> ModItems.BUNDLES.get("red").get();
            case BLACK -> ModItems.BUNDLES.get("black").get();
            case PURPLE -> ModItems.BUNDLES.get("purple").get();
            default -> Items.BUNDLE;
        };
    }

    public static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    public static void playInsertFailSound(Entity entity) {
        entity.playSound(ModSounds.BUNDLE_INSERT_FAIL.get(), 1.0F, 1.0F);
    }

    public static void broadcastChangesOnContainerMenu(Player player) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu.stillValid(player)) {
            menu.slotsChanged(player.getInventory());
        }
    }
}
