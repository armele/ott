package com.otterly76.ott.handler;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

public class CreativeTabHandler {
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab.TabVisibility visibility = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            insertAllAfter(event, Items.CHERRY_BUTTON, List.of(
                    ModBlocks.PALE_OAK_LOG,
                    ModBlocks.PALE_OAK_WOOD,
                    ModBlocks.STRIPPED_PALE_OAK_LOG,
                    ModBlocks.STRIPPED_PALE_OAK_WOOD,
                    ModBlocks.PALE_OAK_PLANKS,
                    ModBlocks.PALE_OAK_STAIRS,
                    ModBlocks.PALE_OAK_SLAB,
                    ModBlocks.PALE_OAK_FENCE,
                    ModBlocks.PALE_OAK_FENCE_GATE,
                    ModBlocks.PALE_OAK_DOOR,
                    ModBlocks.PALE_OAK_TRAPDOOR,
                    ModBlocks.PALE_OAK_PRESSURE_PLATE,
                    ModBlocks.PALE_OAK_BUTTON
            ), visibility);

            insertAllAfter(event, Items.MUD_BRICK_WALL, List.of(
                    ModBlocks.RESIN_BRICKS,
                    ModBlocks.RESIN_BRICK_STAIRS,
                    ModBlocks.RESIN_BRICK_SLAB,
                    ModBlocks.RESIN_BRICK_WALL,
                    ModBlocks.CHISELED_RESIN_BRICKS
            ), visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            insertAllAfter(event, Items.MOSS_CARPET, List.of(
                    ModBlocks.PALE_MOSS_BLOCK,
                    ModBlocks.PALE_MOSS_CARPET,
                    ModBlocks.PALE_HANGING_MOSS
            ), visibility);

            event.insertAfter(Items.CHERRY_LOG.getDefaultInstance(), new ItemStack(ModBlocks.PALE_OAK_LOG.get()), visibility);
            event.insertAfter(Items.CHERRY_LEAVES.getDefaultInstance(), new ItemStack(ModBlocks.PALE_OAK_LEAVES.get()), visibility);
            event.insertAfter(Items.CHERRY_SAPLING.getDefaultInstance(), new ItemStack(ModBlocks.PALE_OAK_SAPLING.get()), visibility);

            insertAllAfter(event, Items.FERN, List.of(ModBlocks.SHORT_DRY_GRASS, ModBlocks.BUSH), visibility);
            insertAllAfter(event, Items.TORCHFLOWER, List.of(ModBlocks.CACTUS_FLOWER, ModBlocks.CLOSED_EYEBLOSSOM, ModBlocks.OPEN_EYEBLOSSOM), visibility);
            insertAllAfter(event, Items.PINK_PETALS, List.of(ModBlocks.WILDFLOWERS, ModBlocks.LEAF_LITTER), visibility);

            event.insertAfter(Items.SPORE_BLOSSOM.getDefaultInstance(), new ItemStack(ModBlocks.FIREFLY_BUSH.get()), visibility);
            event.insertAfter(Items.LARGE_FERN.getDefaultInstance(), new ItemStack(ModBlocks.TALL_DRY_GRASS.get()), visibility);
            event.insertAfter(Items.SNIFFER_EGG.getDefaultInstance(), new ItemStack(ModBlocks.DRIED_GHAST.get()), visibility);
            event.insertAfter(Items.HONEY_BLOCK.getDefaultInstance(), new ItemStack(ModBlocks.RESIN_BLOCK.get()), visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            insertAllAfter(event, Items.CHERRY_HANGING_SIGN, List.of(
                    ModBlocks.PALE_OAK_SIGN,
                    ModBlocks.PALE_OAK_HANGING_SIGN
            ), visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            insertAllAfter(event, Items.CHERRY_CHEST_BOAT, List.of(
                    ModItems.PALE_OAK_BOAT,
                    ModItems.PALE_OAK_CHEST_BOAT
            ), visibility);

            insertAllAfter(event, Items.MUSIC_DISC_RELIC, List.of(
                    ModItems.MUSIC_DISC_TEARS,
                    ModItems.MUSIC_DISC_LAVA_CHICKEN
            ), visibility);

            for (DeferredItem<Item> harness : ModItems.HARNESSES.values()) {
                event.insertAfter(Items.SADDLE.getDefaultInstance(), new ItemStack(harness.get()), visibility);
            }

            if (event.hasPermissions()) {
                for (DeferredItem<Item> bundle : ModItems.BUNDLES.values()) {
                    event.insertAfter(Items.BUNDLE.getDefaultInstance(), new ItemStack(bundle.get()), visibility);
                }
            }
        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.PALE_OAK_PRESSURE_PLATE, visibility);
            event.accept(ModBlocks.PALE_OAK_BUTTON, visibility);
            event.accept(ModBlocks.PALE_OAK_DOOR, visibility);
            event.accept(ModBlocks.PALE_OAK_TRAPDOOR, visibility);
            event.accept(ModBlocks.PALE_OAK_FENCE_GATE, visibility);

            ModBlocks.WOOD_SETS.values().forEach(set -> {
                event.accept(set.pressurePlate(), visibility);
                event.accept(set.button(), visibility);
                event.accept(set.door(), visibility);
                event.accept(set.trapdoor(), visibility);
                event.accept(set.fenceGate(), visibility);
            });
        }


        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.insertAfter(Items.NETHER_BRICK.getDefaultInstance(), new ItemStack(ModItems.RESIN_BRICK.get()), visibility);
            event.insertAfter(Items.HONEYCOMB.getDefaultInstance(), new ItemStack(ModBlocks.RESIN_CLUMP.get()), visibility);
            insertAllAfter(event, Items.EGG, List.of(ModItems.BROWN_EGG, ModItems.BLUE_EGG), visibility);

            event.accept(ModItems.TINY_COAL);
            event.accept(ModItems.TINY_CHARCOAL);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.BROWN_EGG);
            event.accept(ModItems.BLUE_EGG);
        }

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModBlocks.CREAKING_HEART);
        }
    }

    private static void insertAllAfter(BuildCreativeModeTabContentsEvent event, ItemLike target, List<?> items, CreativeModeTab.TabVisibility visibility) {
        ItemStack targetStack = new ItemStack(target);
        // We iterate backwards to maintain the list's order when repeatedly inserting after the same target
        for (int i = items.size() - 1; i >= 0; i--) {
            Object item = items.get(i);
            ItemStack stack;
            if (item instanceof java.util.function.Supplier<?> s) {
                stack = new ItemStack((ItemLike) s.get());
            } else {
                stack = new ItemStack((ItemLike) item);
            }
            event.insertAfter(targetStack, stack, visibility);
        }
    }
}