package com.otterly76.ott.handler;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.function.Supplier;

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
            safeInsertAfter(event, new ItemStack(lastLog), new ItemStack(ModBlocks.PALE_OAK_LOG.get()), visibility);
            lastLog = ModBlocks.PALE_OAK_LOG.get();
            safeInsertAfter(event, new ItemStack(lastLeaves), new ItemStack(ModBlocks.PALE_OAK_LEAVES.get()), visibility);
            lastLeaves = ModBlocks.PALE_OAK_LEAVES.get();
            safeInsertAfter(event, new ItemStack(lastSapling), new ItemStack(ModBlocks.PALE_OAK_SAPLING.get()), visibility);
            lastSapling = ModBlocks.PALE_OAK_SAPLING.get();

            // All wood sets
            for (ModBlocks.WoodSetBlocks set : ModBlocks.WOOD_SETS.values()) {
                safeInsertAfter(event, new ItemStack(lastLog), new ItemStack(set.log().get()), visibility);
                lastLog = set.log().get();
                safeInsertAfter(event, new ItemStack(lastLeaves), new ItemStack(set.leaves().get()), visibility);
                lastLeaves = set.leaves().get();
                safeInsertAfter(event, new ItemStack(lastSapling), new ItemStack(set.sapling().get()), visibility);
                lastSapling = set.sapling().get();
            }

            // Hedges after Leaves
            ItemLike lastHedge = lastLeaves;
            safeInsertAfter(event, new ItemStack(lastHedge), new ItemStack(ModBlocks.THORNY_HEDGE.get()), visibility);
            lastHedge = ModBlocks.THORNY_HEDGE.get();
            safeInsertAfter(event, new ItemStack(lastHedge), new ItemStack(ModItems.THORNY_HEDGE_SPROUTS.get()), visibility);
            lastHedge = ModItems.THORNY_HEDGE_SPROUTS.get();
            for (DeferredBlock<Block> hedge : ModBlocks.PARTICLE_HEDGES.values()) {
                safeInsertAfter(event, new ItemStack(lastHedge), new ItemStack(hedge.get()), visibility);
                lastHedge = hedge.get();
            }
            for (DeferredBlock<Block> hedge : ModBlocks.CREEPING_HEDGES.values()) {
                safeInsertAfter(event, new ItemStack(lastHedge), new ItemStack(hedge.get()), visibility);
                lastHedge = hedge.get();
            }

            insertAllAfter(event, Items.FERN, List.of(ModBlocks.SHORT_DRY_GRASS, ModBlocks.BUSH), visibility);
            insertAllAfter(event, Items.TORCHFLOWER, List.of(ModBlocks.CACTUS_FLOWER, ModBlocks.CLOSED_EYEBLOSSOM, ModBlocks.OPEN_EYEBLOSSOM), visibility);
            insertAllAfter(event, Items.PINK_PETALS, List.of(ModBlocks.WILDFLOWERS, ModBlocks.LEAF_LITTER), visibility);

            safeInsertAfter(event, Items.SPORE_BLOSSOM.getDefaultInstance(), new ItemStack(ModBlocks.FIREFLY_BUSH.get()), visibility);
            safeInsertAfter(event, Items.LARGE_FERN.getDefaultInstance(), new ItemStack(ModBlocks.TALL_DRY_GRASS.get()), visibility);
            safeInsertAfter(event, Items.SNIFFER_EGG.getDefaultInstance(), new ItemStack(ModBlocks.DRIED_GHAST.get()), visibility);
            safeInsertAfter(event, Items.HONEY_BLOCK.getDefaultInstance(), new ItemStack(ModBlocks.RESIN_BLOCK.get()), visibility);
            safeInsertAfter(event, new ItemStack(Items.LILY_PAD), new ItemStack(ModBlocks.BIG_LILY_PAD.get()), visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // Find oxidized copper bulb as target for copper building blocks
            ItemLike lastTarget = Items.OXIDIZED_COPPER_BULB;

            for (Supplier<? extends Block> block : ModBlocks.COPPER_BARS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }
            for (Supplier<? extends Block> block : ModBlocks.COPPER_CHAINS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }
            for (Supplier<? extends Block> block : ModBlocks.COPPER_BUTTONS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }
            for (Supplier<? extends Block> block : ModBlocks.COPPER_PRESSURE_PLATES.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            ItemLike lastTarget = Items.CHERRY_HANGING_SIGN;

            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModItems.PALE_OAK_SIGN.get()), visibility);
            lastTarget = ModItems.PALE_OAK_SIGN.get();
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModItems.PALE_OAK_HANGING_SIGN.get()), visibility);
            lastTarget = ModItems.PALE_OAK_HANGING_SIGN.get();

            for (String setName : ModBlocks.WOOD_SETS.keySet()) {
                Item sign = ModItems.WOOD_SET_SIGNS.get(setName).get();
                Item hangingSign = ModItems.WOOD_SET_HANGING_SIGNS.get(setName).get();

                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(sign), visibility);
                lastTarget = sign;
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(hangingSign), visibility);
                lastTarget = hangingSign;
            }

            // Copper Functional Blocks
            lastTarget = Items.LANTERN;
            for (Supplier<? extends Block> block : ModBlocks.COPPER_LANTERNS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.SOUL_LANTERN;
            for (Supplier<? extends Block> block : ModBlocks.COPPER_SOUL_LANTERNS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.CHEST;
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.COPPER_CHEST.get();
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.EXPOSED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.EXPOSED_COPPER_CHEST.get();
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.WEATHERED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.WEATHERED_COPPER_CHEST.get();
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.OXIDIZED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.OXIDIZED_COPPER_CHEST.get();

            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.WAXED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.WAXED_COPPER_CHEST.get();
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get();
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get()), visibility);
            lastTarget = ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get();
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get()), visibility);

            lastTarget = Items.HOPPER;
            for (Supplier<? extends Block> block : ModBlocks.COPPER_HOPPERS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.CAULDRON;
            for (Supplier<? extends Block> block : ModBlocks.COPPER_CAULDRONS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.CHAIN;
            for (Supplier<? extends Block> block : ModBlocks.COPPER_CHAINS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.LADDER;
            for (Supplier<? extends Block> block : ModBlocks.COPPER_LADDERS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.LIGHTNING_ROD;
            for (Supplier<? extends Block> block : ModBlocks.LIGHTNING_RODS.values()) {
                Block b = block.get();
                if (b == Blocks.LIGHTNING_ROD) {
                    lastTarget = b;
                    continue;
                }
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(b), visibility);
                lastTarget = b;
            }
            
            for (Supplier<? extends com.otterly76.ott.block.custom.CopperGolemStatueBlock> block : ModBlocks.COPPER_GOLEM_STATUES.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            lastTarget = Items.SOUL_TORCH; // After soul torch
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.COPPER_TORCH.get()), visibility);

            lastTarget = Items.CHISELED_BOOKSHELF;
            for (DeferredBlock<com.otterly76.ott.block.shelf.ShelfBlock> block : ModBlocks.SHELVES) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            safeInsertAfter(event, new ItemStack(Items.DRAGON_HEAD), new ItemStack(ModItems.DRAGON_SKULL.get()), visibility);

            lastTarget = Items.DAMAGED_ANVIL;
            for (Supplier<? extends Block> block : ModBlocks.COPPER_ANVILS.values()) {
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(block.get()), visibility);
                lastTarget = block.get();
            }

            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModBlocks.WEATHERING_STATION.get()), visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            ItemLike lastTarget = Items.CHERRY_CHEST_BOAT;

            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModItems.PALE_OAK_BOAT.get()), visibility);
            lastTarget = ModItems.PALE_OAK_BOAT.get();
            safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(ModItems.PALE_OAK_CHEST_BOAT.get()), visibility);
            lastTarget = ModItems.PALE_OAK_CHEST_BOAT.get();

            for (String setName : ModBlocks.WOOD_SETS.keySet()) {
                Item boat = ModItems.WOOD_SET_BOATS.get(setName).get();
                Item chestBoat = ModItems.WOOD_SET_CHEST_BOATS.get(setName).get();

                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(boat), visibility);
                lastTarget = boat;
                safeInsertAfter(event, new ItemStack(lastTarget), new ItemStack(chestBoat), visibility);
                lastTarget = chestBoat;
            }

            insertAllAfter(event, Items.IRON_HOE, List.of(
                    ModItems.COPPER_SHOVEL,
                    ModItems.COPPER_PICKAXE,
                    ModItems.COPPER_AXE,
                    ModItems.COPPER_HOE,
                    ModItems.EXPOSED_COPPER_SHOVEL,
                    ModItems.EXPOSED_COPPER_PICKAXE,
                    ModItems.EXPOSED_COPPER_AXE,
                    ModItems.EXPOSED_COPPER_HOE,
                    ModItems.WEATHERED_COPPER_SHOVEL,
                    ModItems.WEATHERED_COPPER_PICKAXE,
                    ModItems.WEATHERED_COPPER_AXE,
                    ModItems.WEATHERED_COPPER_HOE,
                    ModItems.OXIDIZED_COPPER_SHOVEL,
                    ModItems.OXIDIZED_COPPER_PICKAXE,
                    ModItems.OXIDIZED_COPPER_AXE,
                    ModItems.OXIDIZED_COPPER_HOE
            ), visibility);

            insertAllAfter(event, Items.SHEARS, List.of(
                    ModItems.COPPER_SHEARS,
                    ModItems.EXPOSED_COPPER_SHEARS,
                    ModItems.WEATHERED_COPPER_SHEARS,
                    ModItems.OXIDIZED_COPPER_SHEARS
            ), visibility);

            insertAllAfter(event, Items.MILK_BUCKET, List.of(
                    ModItems.COPPER_BUCKET,
                    ModItems.COPPER_WATER_BUCKET,
                    ModItems.COPPER_LAVA_BUCKET,
                    ModItems.COPPER_POWDER_SNOW_BUCKET,
                    ModItems.COPPER_MILK_BUCKET
            ), visibility);

            insertAllAfter(event, Items.MUSIC_DISC_RELIC, List.of(
                    ModItems.MUSIC_DISC_TEARS,
                    ModItems.MUSIC_DISC_LAVA_CHICKEN
            ), visibility);

            for (DeferredItem<Item> harness : ModItems.HARNESSES.values()) {
                safeInsertAfter(event, Items.SADDLE.getDefaultInstance(), new ItemStack(harness.get()), visibility);
            }

            if (event.hasPermissions()) {
                for (DeferredItem<Item> bundle : ModItems.BUNDLES.values()) {
                    safeInsertAfter(event, Items.BUNDLE.getDefaultInstance(), new ItemStack(bundle.get()), visibility);
                }
            }
        }

        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            safeInsertAfter(event, new ItemStack(ModItems.COPPER_MILK_BUCKET.get()), visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            safeAccept(event, Items.SPRUCE_BUTTON, visibility);
            safeAccept(event, Items.BIRCH_BUTTON, visibility);
            safeAccept(event, Items.JUNGLE_BUTTON, visibility);
            safeAccept(event, Items.ACACIA_BUTTON, visibility);
            safeAccept(event, Items.DARK_OAK_BUTTON, visibility);
            safeAccept(event, Items.MANGROVE_BUTTON, visibility);
            safeAccept(event, Items.CHERRY_BUTTON, visibility);
            safeAccept(event, Items.BAMBOO_BUTTON, visibility);
            safeAccept(event, Items.CRIMSON_BUTTON, visibility);
            safeAccept(event, Items.WARPED_BUTTON, visibility);
            safeAccept(event, Items.POLISHED_BLACKSTONE_BUTTON, visibility);

            safeAccept(event, Items.SPRUCE_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.BIRCH_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.JUNGLE_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.ACACIA_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.DARK_OAK_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.MANGROVE_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.CHERRY_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.BAMBOO_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.CRIMSON_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.WARPED_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.LIGHT_WEIGHTED_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.HEAVY_WEIGHTED_PRESSURE_PLATE, visibility);
            safeAccept(event, Items.POLISHED_BLACKSTONE_PRESSURE_PLATE, visibility);

            safeAccept(event, Items.TRAPPED_CHEST, visibility);
            safeAccept(event, Items.ENDER_CHEST, visibility);
            safeAccept(event, Items.BARREL, visibility);
            safeAccept(event, ModBlocks.COPPER_CHEST.get(), visibility);
            safeAccept(event, ModBlocks.EXPOSED_COPPER_CHEST.get(), visibility);
            safeAccept(event, ModBlocks.WEATHERED_COPPER_CHEST.get(), visibility);
            safeAccept(event, ModBlocks.OXIDIZED_COPPER_CHEST.get(), visibility);
            safeAccept(event, ModBlocks.WAXED_COPPER_CHEST.get(), visibility);
            safeAccept(event, ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get(), visibility);
            safeAccept(event, ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get(), visibility);
            safeAccept(event, ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get(), visibility);

            for (Supplier<? extends Block> block : ModBlocks.COPPER_CAULDRONS.values()) {
                safeAccept(event, block.get(), visibility);
            }

            for (Supplier<? extends Block> block : ModBlocks.LIGHTNING_RODS.values()) {
                safeAccept(event, block.get(), visibility);
            }

            safeAccept(event, Items.SPRUCE_DOOR, visibility);
            safeAccept(event, Items.BIRCH_DOOR, visibility);
            safeAccept(event, Items.JUNGLE_DOOR, visibility);
            safeAccept(event, Items.ACACIA_DOOR, visibility);
            safeAccept(event, Items.DARK_OAK_DOOR, visibility);
            safeAccept(event, Items.MANGROVE_DOOR, visibility);
            safeAccept(event, Items.CHERRY_DOOR, visibility);
            safeAccept(event, Items.BAMBOO_DOOR, visibility);
            safeAccept(event, Items.CRIMSON_DOOR, visibility);
            safeAccept(event, Items.WARPED_DOOR, visibility);
            for (Supplier<? extends Block> block : ModBlocks.COPPER_DOORS.values()) {
                safeAccept(event, block.get(), visibility);
            }

            safeAccept(event, Items.SPRUCE_FENCE_GATE, visibility);
            safeAccept(event, Items.BIRCH_FENCE_GATE, visibility);
            safeAccept(event, Items.JUNGLE_FENCE_GATE, visibility);
            safeAccept(event, Items.ACACIA_FENCE_GATE, visibility);
            safeAccept(event, Items.DARK_OAK_FENCE_GATE, visibility);
            safeAccept(event, Items.MANGROVE_FENCE_GATE, visibility);
            safeAccept(event, Items.CHERRY_FENCE_GATE, visibility);
            safeAccept(event, Items.BAMBOO_FENCE_GATE, visibility);
            safeAccept(event, Items.CRIMSON_FENCE_GATE, visibility);
            safeAccept(event, Items.WARPED_FENCE_GATE, visibility);

            safeAccept(event, Items.SPRUCE_TRAPDOOR, visibility);
            safeAccept(event, Items.BIRCH_TRAPDOOR, visibility);
            safeAccept(event, Items.JUNGLE_TRAPDOOR, visibility);
            safeAccept(event, Items.ACACIA_TRAPDOOR, visibility);
            safeAccept(event, Items.DARK_OAK_TRAPDOOR, visibility);
            safeAccept(event, Items.MANGROVE_TRAPDOOR, visibility);
            safeAccept(event, Items.CHERRY_TRAPDOOR, visibility);
            safeAccept(event, Items.BAMBOO_TRAPDOOR, visibility);
            safeAccept(event, Items.CRIMSON_TRAPDOOR, visibility);
            safeAccept(event, Items.WARPED_TRAPDOOR, visibility);
            for (Supplier<? extends Block> block : ModBlocks.COPPER_TRAPDOORS.values()) {
                safeAccept(event, block.get(), visibility);
            }

            safeAccept(event, Items.SPRUCE_CHEST_BOAT, visibility);
            safeAccept(event, Items.BIRCH_CHEST_BOAT, visibility);
            safeAccept(event, Items.JUNGLE_CHEST_BOAT, visibility);
            safeAccept(event, Items.ACACIA_CHEST_BOAT, visibility);
            safeAccept(event, Items.DARK_OAK_CHEST_BOAT, visibility);
            safeAccept(event, Items.MANGROVE_CHEST_BOAT, visibility);
            safeAccept(event, Items.CHERRY_CHEST_BOAT, visibility);
            safeAccept(event, Items.BAMBOO_CHEST_RAFT, visibility);
            safeAccept(event, ModItems.PALE_OAK_CHEST_BOAT.get(), visibility);
            for (DeferredItem<? extends Item> boat : ModItems.WOOD_SET_CHEST_BOATS.values()) {
                safeAccept(event, boat.get(), visibility);
            }

            safeAccept(event, ModBlocks.PALE_OAK_PRESSURE_PLATE.get(), visibility);
            safeAccept(event, ModBlocks.PALE_OAK_BUTTON.get(), visibility);
            safeAccept(event, ModBlocks.PALE_OAK_DOOR.get(), visibility);
            safeAccept(event, ModBlocks.PALE_OAK_TRAPDOOR.get(), visibility);
            safeAccept(event, ModBlocks.PALE_OAK_FENCE_GATE.get(), visibility);

            ModBlocks.WOOD_SETS.values().forEach(set -> {
                safeAccept(event, set.pressurePlate(), visibility);
                safeAccept(event, set.button(), visibility);
                safeAccept(event, set.door(), visibility);
                safeAccept(event, set.trapdoor(), visibility);
                safeAccept(event, set.fenceGate(), visibility);
            });

            for (Supplier<? extends Block> block : ModBlocks.COPPER_BUTTONS.values()) {
                safeAccept(event, block.get(), visibility);
            }

            for (Supplier<? extends Block> block : ModBlocks.COPPER_PRESSURE_PLATES.values()) {
                safeAccept(event, block.get(), visibility);
            }

            for (Supplier<? extends Block> block : ModBlocks.COPPER_HOPPERS.values()) {
                safeAccept(event, block.get(), visibility);
            }

            ItemLike lastRailTarget = Items.RAIL;
            for (Supplier<? extends Block> block : ModBlocks.COPPER_RAILS.values()) {
                safeInsertAfter(event, new ItemStack(lastRailTarget), new ItemStack(block.get()), visibility);
                lastRailTarget = block.get();
            }
        }


        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            safeAccept(event, Items.CHAIN, visibility);
            safeInsertAfter(event, Items.NETHER_BRICK.getDefaultInstance(), new ItemStack(ModItems.RESIN_BRICK.get()), visibility);
            safeInsertAfter(event, Items.HONEYCOMB.getDefaultInstance(), new ItemStack(ModBlocks.RESIN_CLUMP.get()), visibility);
            insertAllAfter(event, Items.EGG, List.of(ModItems.BROWN_EGG, ModItems.BLUE_EGG), visibility);

            safeInsertAfter(event, Items.COPPER_INGOT.getDefaultInstance(), new ItemStack(ModItems.COPPER_NUGGET.get()), visibility);
            safeInsertAfter(event, ModItems.COPPER_NUGGET.get().getDefaultInstance(), new ItemStack(ModBlocks.COPPER_CHAINS.get("").get()), visibility);
            safeInsertAfter(event, ModBlocks.COPPER_CHAINS.get("").get().asItem().getDefaultInstance(), new ItemStack(ModBlocks.COPPER_CHAINS.get("exposed_").get()), visibility);
            safeInsertAfter(event, ModBlocks.COPPER_CHAINS.get("exposed_").get().asItem().getDefaultInstance(), new ItemStack(ModBlocks.COPPER_CHAINS.get("weathered_").get()), visibility);
            safeInsertAfter(event, ModBlocks.COPPER_CHAINS.get("weathered_").get().asItem().getDefaultInstance(), new ItemStack(ModBlocks.COPPER_CHAINS.get("oxidized_").get()), visibility);

            safeAccept(event, ModItems.TINY_COAL, visibility);
            safeAccept(event, ModItems.TINY_CHARCOAL, visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            insertAllAfter(event, Items.IRON_SWORD, List.of(
                    ModItems.COPPER_SWORD,
                    ModItems.EXPOSED_COPPER_SWORD,
                    ModItems.WEATHERED_COPPER_SWORD,
                    ModItems.OXIDIZED_COPPER_SWORD
            ), visibility);

            insertAllAfter(event, Items.IRON_AXE, List.of(
                    ModItems.COPPER_AXE,
                    ModItems.EXPOSED_COPPER_AXE,
                    ModItems.WEATHERED_COPPER_AXE,
                    ModItems.OXIDIZED_COPPER_AXE
            ), visibility);
            insertAllAfter(event, Items.IRON_BOOTS, List.of(
                    ModItems.COPPER_HELMET,
                    ModItems.COPPER_CHESTPLATE,
                    ModItems.COPPER_LEGGINGS,
                    ModItems.COPPER_BOOTS,
                    ModItems.EXPOSED_COPPER_HELMET,
                    ModItems.EXPOSED_COPPER_CHESTPLATE,
                    ModItems.EXPOSED_COPPER_LEGGINGS,
                    ModItems.EXPOSED_COPPER_BOOTS,
                    ModItems.WEATHERED_COPPER_HELMET,
                    ModItems.WEATHERED_COPPER_CHESTPLATE,
                    ModItems.WEATHERED_COPPER_LEGGINGS,
                    ModItems.WEATHERED_COPPER_BOOTS,
                    ModItems.OXIDIZED_COPPER_HELMET,
                    ModItems.OXIDIZED_COPPER_CHESTPLATE,
                    ModItems.OXIDIZED_COPPER_LEGGINGS,
                    ModItems.OXIDIZED_COPPER_BOOTS
            ), visibility);

            insertAllAfter(event, Items.CHAINMAIL_BOOTS, List.of(
                    ModItems.COPPER_CHAINMAIL_HELMET,
                    ModItems.COPPER_CHAINMAIL_CHESTPLATE,
                    ModItems.COPPER_CHAINMAIL_LEGGINGS,
                    ModItems.COPPER_CHAINMAIL_BOOTS,
                    ModItems.EXPOSED_COPPER_CHAINMAIL_HELMET,
                    ModItems.EXPOSED_COPPER_CHAINMAIL_CHESTPLATE,
                    ModItems.EXPOSED_COPPER_CHAINMAIL_LEGGINGS,
                    ModItems.EXPOSED_COPPER_CHAINMAIL_BOOTS,
                    ModItems.WEATHERED_COPPER_CHAINMAIL_HELMET,
                    ModItems.WEATHERED_COPPER_CHAINMAIL_CHESTPLATE,
                    ModItems.WEATHERED_COPPER_CHAINMAIL_LEGGINGS,
                    ModItems.WEATHERED_COPPER_CHAINMAIL_BOOTS,
                    ModItems.OXIDIZED_COPPER_CHAINMAIL_HELMET,
                    ModItems.OXIDIZED_COPPER_CHAINMAIL_CHESTPLATE,
                    ModItems.OXIDIZED_COPPER_CHAINMAIL_LEGGINGS,
                    ModItems.OXIDIZED_COPPER_CHAINMAIL_BOOTS
            ), visibility);

            safeInsertAfter(event, Items.GOLDEN_HORSE_ARMOR.getDefaultInstance(), new ItemStack(ModItems.COPPER_HORSE_ARMOR.get()), visibility);
            safeInsertAfter(event, ModItems.COPPER_HORSE_ARMOR.get().getDefaultInstance(), new ItemStack(ModItems.EXPOSED_COPPER_HORSE_ARMOR.get()), visibility);
            safeInsertAfter(event, ModItems.EXPOSED_COPPER_HORSE_ARMOR.get().getDefaultInstance(), new ItemStack(ModItems.WEATHERED_COPPER_HORSE_ARMOR.get()), visibility);
            safeInsertAfter(event, ModItems.WEATHERED_COPPER_HORSE_ARMOR.get().getDefaultInstance(), new ItemStack(ModItems.OXIDIZED_COPPER_HORSE_ARMOR.get()), visibility);
            safeInsertAfter(event, Items.DIAMOND_HORSE_ARMOR.getDefaultInstance(), new ItemStack(ModItems.NETHERITE_HORSE_ARMOR.get()), visibility);

            safeInsertAfter(event, Items.TIPPED_ARROW.getDefaultInstance(), new ItemStack(ModItems.TORCH_ARROW.get()), visibility);

            safeAccept(event, ModItems.BROWN_EGG, visibility);
            safeAccept(event, ModItems.BLUE_EGG, visibility);
        }

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            safeAccept(event, ModBlocks.CREAKING_HEART, visibility);
            safeAccept(event, ModItems.CREAKING_SPAWN_EGG, visibility);
            safeAccept(event, ModItems.HAPPY_GHAST_SPAWN_EGG, visibility);

            safeAccept(event, ModItems.COPPER_GOLEM_SPAWN_EGG, visibility);
            
            safeAccept(event, ModItems.DUCK_SPAWN_EGG, visibility);
            safeAccept(event, ModItems.GOOSE_SPAWN_EGG, visibility);

            safeAccept(event, ModItems.MAN_O_WAR_SPAWN_EGG, visibility);
            safeAccept(event, ModItems.MAN_O_WAR_BUCKET, visibility);
        }
    }

    private static void insertAllAfter(BuildCreativeModeTabContentsEvent event, ItemLike target, List<?> items, CreativeModeTab.TabVisibility visibility) {
        // We iterate backwards to maintain the list's order when repeatedly inserting after the same target
        for (int i = items.size() - 1; i >= 0; i--) {
            Object item = items.get(i);
            ItemStack stack;
            if (item instanceof java.util.function.Supplier<?> s) {
                stack = new ItemStack((ItemLike) s.get());
            } else {
                stack = new ItemStack((ItemLike) item);
            }
            safeInsertAfter(event, new ItemStack(target), stack, visibility);
        }
    }

    private static void safeAccept(BuildCreativeModeTabContentsEvent event, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        try {
            event.accept(item, visibility);
        } catch (IllegalArgumentException e) {
            // Item already exists in the tab, ignore the error to avoid crashing.
        }
    }

    private static void safeInsertAfter(BuildCreativeModeTabContentsEvent event, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
        safeInsertAfter(event, new ItemStack(Items.MILK_BUCKET), stack, visibility);
    }

    private static void safeInsertAfter(BuildCreativeModeTabContentsEvent event, ItemStack target, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
        try {
            event.insertAfter(target, stack, visibility);
        } catch (Exception e) {
            // Target might be missing, try to just accept it.
            // Note: If the item already exists in the tab, NeoForge throws an IllegalArgumentException.
            try {
                event.accept(stack, visibility);
            } catch (IllegalArgumentException e2) {
                // Item already exists in the tab, ignore the error to avoid crashing.
            }
        }
    }
}