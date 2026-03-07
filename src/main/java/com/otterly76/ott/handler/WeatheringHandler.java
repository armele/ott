package com.otterly76.ott.handler;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;
import java.util.Optional;

public class WeatheringHandler {
    private static final BiMap<Block, Block> NEXT_BY_STATE = HashBiMap.create();
    private static final BiMap<Block, Block> WAX_ON_BY_BLOCK = HashBiMap.create();
    private static final BiMap<Item, Item> NEXT_BY_ITEM = HashBiMap.create();
    private static final BiMap<Item, Item> WAX_ON_BY_ITEM = HashBiMap.create();

    public static void registerTransitions() {
        // Blocks
        registerBlockTransitions();

        // Items
        registerItemTransitions();
    }

    private static void registerBlockTransitions() {
        // Copper Chests
        addChain(ModBlocks.COPPER_CHEST.get(), ModBlocks.EXPOSED_COPPER_CHEST.get(), ModBlocks.WEATHERED_COPPER_CHEST.get(), ModBlocks.OXIDIZED_COPPER_CHEST.get());
        addWaxing(ModBlocks.COPPER_CHEST.get(), ModBlocks.WAXED_COPPER_CHEST.get());
        addWaxing(ModBlocks.EXPOSED_COPPER_CHEST.get(), ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get());
        addWaxing(ModBlocks.WEATHERED_COPPER_CHEST.get(), ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get());
        addWaxing(ModBlocks.OXIDIZED_COPPER_CHEST.get(), ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get());

        // Buttons, Lanterns, Chains, Bars, Lightning Rods
        String[] states = {"", "exposed_", "weathered_", "oxidized_"};
        addChainFromMap(ModBlocks.COPPER_BUTTONS, states);
        addChainFromMap(ModBlocks.COPPER_LANTERNS, states);
        addChainFromMap(ModBlocks.COPPER_CHAINS, states);
        addChainFromMap(ModBlocks.COPPER_BARS, states);
        addChainFromMap(ModBlocks.LIGHTNING_RODS, states);

        // Statues
        for (int i = 0; i < 3; i++) {
            addWeatheringTransition(ModBlocks.COPPER_GOLEM_STATUES.get(i*2).get(), ModBlocks.COPPER_GOLEM_STATUES.get((i+1)*2).get());
        }
        for (int i = 0; i < 4; i++) {
            addWaxingTransition(ModBlocks.COPPER_GOLEM_STATUES.get(i*2).get(), ModBlocks.COPPER_GOLEM_STATUES.get(i*2+1).get());
        }

        // New Blocks from everythingcopper
        addChainFromMap(ModBlocks.COPPER_ANVILS, states);
        addChainFromMap(ModBlocks.CHIPPED_COPPER_ANVILS, states);
        addChainFromMap(ModBlocks.DAMAGED_COPPER_ANVILS, states);

        addChainFromMap(ModBlocks.COPPER_CAULDRONS, states);
        addChainFromMap(ModBlocks.COPPER_WATER_CAULDRONS, states);
        addChainFromMap(ModBlocks.COPPER_LAVA_CAULDRONS, states);
        addChainFromMap(ModBlocks.COPPER_POWDER_SNOW_CAULDRONS, states);

        addChainFromMap(ModBlocks.COPPER_HOPPERS, states);
        addChainFromMap(ModBlocks.COPPER_LADDERS, states);
        addChainFromMap(ModBlocks.COPPER_PRESSURE_PLATES, states);
        addChainFromMap(ModBlocks.COPPER_RAILS, states);
        addChainFromMap(ModBlocks.COPPER_SOUL_LANTERNS, states);
    }

    private static void registerItemTransitions() {
        String[] states = {"", "exposed_", "weathered_", "oxidized_"};
        String[] tools = {"axe", "pickaxe", "shovel", "hoe", "sword", "shears"};
        String[] armor = {"helmet", "chestplate", "leggings", "boots", "horse_armor"};

        // Map Tool Transitions
        for (String tool : tools) {
            Item base = getItem("minecraft:copper_" + tool);
            Item exposed = getItem("ott:exposed_copper_" + tool);
            Item weathered = getItem("ott:weathered_copper_" + tool);
            Item oxidized = getItem("ott:oxidized_copper_" + tool);

            addItemChain(base, exposed, weathered, oxidized);

            // Waxed versions
            addItemWaxing(base, getItem("ott:waxed_copper_" + tool));
            addItemWaxing(exposed, getItem("ott:waxed_exposed_copper_" + tool));
            addItemWaxing(weathered, getItem("ott:waxed_weathered_copper_" + tool));
            addItemWaxing(oxidized, getItem("ott:waxed_oxidized_copper_" + tool));
        }

        // Map Armor Transitions
        for (String part : armor) {
            Item base = getItem("minecraft:copper_" + part);
            Item exposed = getItem("ott:exposed_copper_" + part);
            Item weathered = getItem("ott:weathered_copper_" + part);
            Item oxidized = getItem("ott:oxidized_copper_" + part);

            addItemChain(base, exposed, weathered, oxidized);

            // Waxed versions
            addItemWaxing(base, getItem("ott:waxed_copper_" + part));
            addItemWaxing(exposed, getItem("ott:waxed_exposed_copper_" + part));
            addItemWaxing(weathered, getItem("ott:waxed_weathered_copper_" + part));
            addItemWaxing(oxidized, getItem("ott:waxed_oxidized_copper_" + part));
        }

        // Add transitions for block items by linking to the block transitions
        for (Map.Entry<Block, Block> entry : NEXT_BY_STATE.entrySet()) {
            addItemWeatheringTransition(entry.getKey().asItem(), entry.getValue().asItem());
        }
        for (Map.Entry<Block, Block> entry : WAX_ON_BY_BLOCK.entrySet()) {
            addItemWaxingTransition(entry.getKey().asItem(), entry.getValue().asItem());
        }
    }

    private static Item getItem(String id) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static void addChainFromMap(Map<String, ? extends DeferredBlock<? extends Block>> map, String[] states) {
        for (int i = 0; i < states.length - 1; i++) {
            DeferredBlock<? extends Block> original = map.get(states[i]);
            DeferredBlock<? extends Block> next = map.get(states[i+1]);
            if (original != null && next != null) {
                addWeatheringTransition(original.get(), next.get());
            } else if (states[i].isEmpty() && next != null) {
                // Special case: if base state is missing (e.g. lightning_rod), use vanilla
                addWeatheringTransition(net.minecraft.world.level.block.Blocks.LIGHTNING_ROD, next.get());
            }
        }
        for (String state : states) {
            DeferredBlock<? extends Block> original = map.get(state);
            DeferredBlock<? extends Block> waxed = map.get("waxed_" + state);
            if (original != null && waxed != null) {
                addWaxingTransition(original.get(), waxed.get());
            } else if (state.isEmpty() && waxed != null) {
                addWaxingTransition(net.minecraft.world.level.block.Blocks.LIGHTNING_ROD, waxed.get());
            }
        }
    }

    private static void addChain(Block b1, Block b2, Block b3, Block b4) {
        addWeatheringTransition(b1, b2);
        addWeatheringTransition(b2, b3);
        addWeatheringTransition(b3, b4);
    }

    private static void addWaxing(Block original, Block waxed) {
        addWaxingTransition(original, waxed);
    }

    private static void addWeatheringTransition(Block original, Block next) {
        if (original != null && next != null) NEXT_BY_STATE.put(original, next);
    }

    private static void addWaxingTransition(Block original, Block waxed) {
        if (original != null && waxed != null) WAX_ON_BY_BLOCK.put(original, waxed);
    }

    private static void addItemChain(Item i1, Item i2, Item i3, Item i4) {
        addItemWeatheringTransition(i1, i2);
        addItemWeatheringTransition(i2, i3);
        addItemWeatheringTransition(i3, i4);
    }

    private static void addItemWaxing(Item original, Item waxed) {
        addItemWaxingTransition(original, waxed);
    }

    private static void addItemWeatheringTransition(Item original, Item next) {
        if (original != null && next != null && original != Items.AIR && next != Items.AIR) NEXT_BY_ITEM.put(original, next);
    }

    private static void addItemWaxingTransition(Item original, Item waxed) {
        if (original != null && waxed != null && original != Items.AIR && waxed != Items.AIR) WAX_ON_BY_ITEM.put(original, waxed);
    }

    public static Optional<Block> getNext(Block block) {
        return Optional.ofNullable(NEXT_BY_STATE.get(block));
    }

    public static Optional<Block> getWaxed(Block block) {
        return Optional.ofNullable(WAX_ON_BY_BLOCK.get(block));
    }

    public static Optional<Block> getUnwaxed(Block block) {
        return Optional.ofNullable(WAX_ON_BY_BLOCK.inverse().get(block));
    }

    public static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(NEXT_BY_STATE.inverse().get(block));
    }

    public static Optional<ItemStack> getNextItem(ItemStack stack) {
        Item nextItem = NEXT_BY_ITEM.get(stack.getItem());
        if (nextItem != null) {
            ItemStack nextStack = stack.copy();
            return Optional.of(nextStack.transmuteCopy(nextItem, 1));
        }
        return Optional.empty();
    }

    public static Optional<ItemStack> getWaxedItem(ItemStack stack) {
        Item waxedItem = WAX_ON_BY_ITEM.get(stack.getItem());
        if (waxedItem != null) {
            ItemStack nextStack = stack.copy();
            return Optional.of(nextStack.transmuteCopy(waxedItem, 1));
        }
        return Optional.empty();
    }
}