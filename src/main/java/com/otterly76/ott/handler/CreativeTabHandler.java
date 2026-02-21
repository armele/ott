package com.otterly76.ott.handler;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

public class CreativeTabHandler {
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab.TabVisibility visibility = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            ItemLike lastTarget = Items.CHERRY_BUTTON;

            // Add Pale Oak components
            List<ItemLike> paleOakBuilding = List.of(
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
            );
            insertAllAfter(event, lastTarget, paleOakBuilding, visibility);
            lastTarget = ModBlocks.PALE_OAK_BUTTON.get();

            // Add components for all wood sets
            for (ModBlocks.WoodSetBlocks set : ModBlocks.WOOD_SETS.values()) {
                List<ItemLike> setBuilding = List.of(
                        set.log(), set.wood(),
                        set.strippedLog(), set.strippedWood(),
                        set.planks(), set.stairs(), set.slab(),
                        set.fence(), set.fenceGate(),
                        set.door(), set.trapdoor(),
                        set.pressurePlate(), set.button()
                );
                insertAllAfter(event, lastTarget, setBuilding, visibility);
                lastTarget = set.button().get();
            }

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
                    ModBlocks.PALE_HANGING_MOSS,
                    ModBlocks.CREAKING_HEART
            ), visibility);

            ItemLike lastLog = Items.CHERRY_LOG;
            ItemLike lastLeaves = Items.CHERRY_LEAVES;
            ItemLike lastSapling = Items.CHERRY_SAPLING;

            // Pale Oak
            event.insertAfter(new ItemStack(lastLog), new ItemStack(ModBlocks.PALE_OAK_LOG.get()), visibility);
            lastLog = ModBlocks.PALE_OAK_LOG.get();
            event.insertAfter(new ItemStack(lastLeaves), new ItemStack(ModBlocks.PALE_OAK_LEAVES.get()), visibility);
            lastLeaves = ModBlocks.PALE_OAK_LEAVES.get();
            event.insertAfter(new ItemStack(lastSapling), new ItemStack(ModBlocks.PALE_OAK_SAPLING.get()), visibility);
            lastSapling = ModBlocks.PALE_OAK_SAPLING.get();

            // All wood sets
            for (ModBlocks.WoodSetBlocks set : ModBlocks.WOOD_SETS.values()) {
                event.insertAfter(new ItemStack(lastLog), new ItemStack(set.log().get()), visibility);
                lastLog = set.log().get();
                event.insertAfter(new ItemStack(lastLeaves), new ItemStack(set.leaves().get()), visibility);
                lastLeaves = set.leaves().get();
                event.insertAfter(new ItemStack(lastSapling), new ItemStack(set.sapling().get()), visibility);
                lastSapling = set.sapling().get();
            }

            // Hedges after Leaves
            ItemLike lastHedge = lastLeaves;
            event.insertAfter(new ItemStack(lastHedge), new ItemStack(ModBlocks.THORNY_HEDGE.get()), visibility);
            lastHedge = ModBlocks.THORNY_HEDGE.get();
            event.insertAfter(new ItemStack(lastHedge), new ItemStack(ModItems.THORNY_HEDGE_SPROUTS.get()), visibility);
            lastHedge = ModItems.THORNY_HEDGE_SPROUTS.get();
            for (DeferredBlock<Block> hedge : ModBlocks.PARTICLE_HEDGES.values()) {
                event.insertAfter(new ItemStack(lastHedge), new ItemStack(hedge.get()), visibility);
                lastHedge = hedge.get();
            }
            for (DeferredBlock<Block> hedge : ModBlocks.CREEPING_HEDGES.values()) {
                event.insertAfter(new ItemStack(lastHedge), new ItemStack(hedge.get()), visibility);
                lastHedge = hedge.get();
            }

            insertAllAfter(event, Items.FERN, List.of(ModBlocks.SHORT_DRY_GRASS, ModBlocks.BUSH), visibility);
            insertAllAfter(event, Items.TORCHFLOWER, List.of(ModBlocks.CACTUS_FLOWER, ModBlocks.CLOSED_EYEBLOSSOM, ModBlocks.OPEN_EYEBLOSSOM), visibility);
            insertAllAfter(event, Items.PINK_PETALS, List.of(ModBlocks.WILDFLOWERS, ModBlocks.LEAF_LITTER), visibility);

            event.insertAfter(Items.SPORE_BLOSSOM.getDefaultInstance(), new ItemStack(ModBlocks.FIREFLY_BUSH.get()), visibility);
            event.insertAfter(Items.LARGE_FERN.getDefaultInstance(), new ItemStack(ModBlocks.TALL_DRY_GRASS.get()), visibility);
            event.insertAfter(Items.SNIFFER_EGG.getDefaultInstance(), new ItemStack(ModBlocks.DRIED_GHAST.get()), visibility);
            event.insertAfter(Items.HONEY_BLOCK.getDefaultInstance(), new ItemStack(ModBlocks.RESIN_BLOCK.get()), visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // Find oxidized copper bulb as target for copper building blocks
            ItemLike lastTarget = Items.OXIDIZED_COPPER_BULB;

            for (DeferredBlock<Block> block : ModBlocks.COPPER_BARS.values()) {
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }
            for (DeferredBlock<Block> block : ModBlocks.COPPER_CHAINS.values()) {
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }
            for (DeferredBlock<Block> block : ModBlocks.COPPER_BUTTONS.values()) {
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            ItemLike lastTarget = Items.CHERRY_HANGING_SIGN;

            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModItems.PALE_OAK_SIGN.get()), visibility);
            lastTarget = ModItems.PALE_OAK_SIGN.get();
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModItems.PALE_OAK_HANGING_SIGN.get()), visibility);
            lastTarget = ModItems.PALE_OAK_HANGING_SIGN.get();

            for (String setName : ModBlocks.WOOD_SETS.keySet()) {
                Item sign = ModItems.WOOD_SET_SIGNS.get(setName).get();
                Item hangingSign = ModItems.WOOD_SET_HANGING_SIGNS.get(setName).get();

                event.insertAfter(new ItemStack(lastTarget), new ItemStack(sign), visibility);
                lastTarget = sign;
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(hangingSign), visibility);
                lastTarget = hangingSign;
            }

            // Copper Functional Blocks
            lastTarget = Items.LANTERN;
            for (DeferredBlock<Block> block : ModBlocks.COPPER_LANTERNS.values()) {
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.CHEST;
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.COPPER_CHEST.get();
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.EXPOSED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.EXPOSED_COPPER_CHEST.get();
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.WEATHERED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.WEATHERED_COPPER_CHEST.get();
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.OXIDIZED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.OXIDIZED_COPPER_CHEST.get();

            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.WAXED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.WAXED_COPPER_CHEST.get();
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get();
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get();
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get()), visibility);

            lastTarget = Items.LIGHTNING_ROD;
            for (DeferredBlock<Block> block : ModBlocks.LIGHTNING_RODS.values()) {
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            for (DeferredBlock<com.otterly76.ott.block.custom.CopperGolemStatueBlock> block : ModBlocks.COPPER_GOLEM_STATUES) {
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.SOUL_TORCH; // After soul torch
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModBlocks.COPPER_TORCH.get()), visibility);

            lastTarget = Items.CHISELED_BOOKSHELF;
            for (DeferredBlock<com.otterly76.ott.block.shelf.ShelfBlock> block : ModBlocks.SHELVES) {
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            ItemLike lastTarget = Items.CHERRY_CHEST_BOAT;

            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModItems.PALE_OAK_BOAT.get()), visibility);
            lastTarget = ModItems.PALE_OAK_BOAT.get();
            event.insertAfter(new ItemStack(lastTarget), new ItemStack(ModItems.PALE_OAK_CHEST_BOAT.get()), visibility);
            lastTarget = ModItems.PALE_OAK_CHEST_BOAT.get();

            for (String setName : ModBlocks.WOOD_SETS.keySet()) {
                Item boat = ModItems.WOOD_SET_BOATS.get(setName).get();
                Item chestBoat = ModItems.WOOD_SET_CHEST_BOATS.get(setName).get();

                event.insertAfter(new ItemStack(lastTarget), new ItemStack(boat), visibility);
                lastTarget = boat;
                event.insertAfter(new ItemStack(lastTarget), new ItemStack(chestBoat), visibility);
                lastTarget = chestBoat;
            }

            insertAllAfter(event, Items.IRON_HOE, List.of(
                    ModItems.COPPER_SHOVEL,
                    ModItems.COPPER_PICKAXE,
                    ModItems.COPPER_AXE,
                    ModItems.COPPER_HOE
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

            for (DeferredBlock<Block> block : ModBlocks.COPPER_BUTTONS.values()) {
                event.accept(block.get(), visibility);
            }
        }


        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.insertAfter(Items.NETHER_BRICK.getDefaultInstance(), new ItemStack(ModItems.RESIN_BRICK.get()), visibility);
            event.insertAfter(Items.HONEYCOMB.getDefaultInstance(), new ItemStack(ModBlocks.RESIN_CLUMP.get()), visibility);
            insertAllAfter(event, Items.EGG, List.of(ModItems.BROWN_EGG, ModItems.BLUE_EGG), visibility);

            event.insertAfter(Items.COPPER_INGOT.getDefaultInstance(), new ItemStack(ModItems.COPPER_NUGGET.get()), visibility);

            event.accept(ModItems.TINY_COAL);
            event.accept(ModItems.TINY_CHARCOAL);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            insertAllAfter(event, Items.IRON_SWORD, List.of(ModItems.COPPER_SWORD), visibility);
            insertAllAfter(event, Items.IRON_BOOTS, List.of(
                    ModItems.COPPER_HELMET,
                    ModItems.COPPER_CHESTPLATE,
                    ModItems.COPPER_LEGGINGS,
                    ModItems.COPPER_BOOTS
            ), visibility);

            event.insertAfter(Items.GOLDEN_HORSE_ARMOR.getDefaultInstance(), new ItemStack(ModItems.COPPER_HORSE_ARMOR.get()), visibility);
            event.insertAfter(Items.DIAMOND_HORSE_ARMOR.getDefaultInstance(), new ItemStack(ModItems.NETHERITE_HORSE_ARMOR.get()), visibility);

            event.insertAfter(Items.TIPPED_ARROW.getDefaultInstance(), new ItemStack(ModItems.TORCH_ARROW.get()), visibility);

            event.accept(ModItems.BROWN_EGG);
            event.accept(ModItems.BLUE_EGG);
        }

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModBlocks.CREAKING_HEART);
            event.accept(ModItems.CREAKING_SPAWN_EGG);
            event.accept(ModItems.HAPPY_GHAST_SPAWN_EGG);

            event.accept(ModItems.TINY_SKELETON_SPAWN_EGG);
            event.accept(ModItems.TINY_CREEPER_SPAWN_EGG);
            event.accept(ModItems.TINY_ENDERMAN_SPAWN_EGG);
            event.accept(ModItems.TINY_BOGGED_SPAWN_EGG);
            event.accept(ModItems.TINY_DROWNED_SPAWN_EGG);
            event.accept(ModItems.TINY_HUSK_SPAWN_EGG);
            event.accept(ModItems.TINY_STRAY_SPAWN_EGG);
            event.accept(ModItems.TINY_WITHER_SKELETON_SPAWN_EGG);

            event.accept(ModItems.COPPER_GOLEM_SPAWN_EGG);

            event.accept(ModItems.MAN_O_WAR_SPAWN_EGG);
            event.accept(ModItems.MAN_O_WAR_BUCKET);
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