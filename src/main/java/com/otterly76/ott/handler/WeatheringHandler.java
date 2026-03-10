package com.otterly76.ott.handler;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

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
        addChainFromMap(ModBlocks.COPPER_SOUL_LANTERNS, states);
        addChainFromMap(ModBlocks.COPPER_CHAINS, states);
        addChainFromMap(ModBlocks.COPPER_BARS, states);
        addChainFromMap(ModBlocks.COPPER_HOPPERS, states);
        addChainFromMap(ModBlocks.COPPER_LADDERS, states);
        addChainFromMap(ModBlocks.COPPER_CAULDRONS, states);
        addChainFromMap(ModBlocks.COPPER_WATER_CAULDRONS, states);
        addChainFromMap(ModBlocks.COPPER_LAVA_CAULDRONS, states);
        addChainFromMap(ModBlocks.COPPER_POWDER_SNOW_CAULDRONS, states);
        addChainFromMap(ModBlocks.COPPER_RAILS, states);
        addChainFromMap(ModBlocks.LIGHTNING_RODS, states);

        for (String damagePrefix : new String[]{"", "chipped_", "damaged_"}) {
            for (int i = 0; i < states.length - 1; i++) {
                addWeatheringTransition(ModBlocks.COPPER_ANVILS.get(damagePrefix + states[i]).get(), ModBlocks.COPPER_ANVILS.get(damagePrefix + states[i+1]).get());
                addWeatheringTransition(ModBlocks.COPPER_ANVILS.get("waxed_" + damagePrefix + states[i]).get(), ModBlocks.COPPER_ANVILS.get("waxed_" + damagePrefix + states[i+1]).get());
            }
            for (String state : states) {
                addWaxingTransition(ModBlocks.COPPER_ANVILS.get(damagePrefix + state).get(), ModBlocks.COPPER_ANVILS.get("waxed_" + damagePrefix + state).get());
            }
        }

        // Statues
        addChainFromMap(ModBlocks.COPPER_GOLEM_STATUES, states);

    }

    private static void registerItemTransitions() {
        String[] states = {"", "exposed_", "weathered_", "oxidized_"};

        // Add transitions for block items by linking to the block transitions
        for (Map.Entry<Block, Block> entry : NEXT_BY_STATE.entrySet()) {
            addItemWeatheringTransition(entry.getKey().asItem(), entry.getValue().asItem());
        }
        for (Map.Entry<Block, Block> entry : WAX_ON_BY_BLOCK.entrySet()) {
            addItemWaxingTransition(entry.getKey().asItem(), entry.getValue().asItem());
        }

        // Horse Armor
        addItemChain(ModItems.COPPER_HORSE_ARMOR.get(), ModItems.EXPOSED_COPPER_HORSE_ARMOR.get(), ModItems.WEATHERED_COPPER_HORSE_ARMOR.get(), ModItems.OXIDIZED_COPPER_HORSE_ARMOR.get());

        // Plate Armor
        addItemChain(ModItems.COPPER_HELMET.get(), ModItems.EXPOSED_COPPER_HELMET.get(), ModItems.WEATHERED_COPPER_HELMET.get(), ModItems.OXIDIZED_COPPER_HELMET.get());
        addItemChain(ModItems.COPPER_CHESTPLATE.get(), ModItems.EXPOSED_COPPER_CHESTPLATE.get(), ModItems.WEATHERED_COPPER_CHESTPLATE.get(), ModItems.OXIDIZED_COPPER_CHESTPLATE.get());
        addItemChain(ModItems.COPPER_LEGGINGS.get(), ModItems.EXPOSED_COPPER_LEGGINGS.get(), ModItems.WEATHERED_COPPER_LEGGINGS.get(), ModItems.OXIDIZED_COPPER_LEGGINGS.get());
        addItemChain(ModItems.COPPER_BOOTS.get(), ModItems.EXPOSED_COPPER_BOOTS.get(), ModItems.WEATHERED_COPPER_BOOTS.get(), ModItems.OXIDIZED_COPPER_BOOTS.get());

        // Chainmail Armor
        addItemChain(ModItems.COPPER_CHAINMAIL_HELMET.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_HELMET.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_HELMET.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_HELMET.get());
        addItemChain(ModItems.COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_CHESTPLATE.get());
        addItemChain(ModItems.COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_LEGGINGS.get());
        addItemChain(ModItems.COPPER_CHAINMAIL_BOOTS.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_BOOTS.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_BOOTS.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_BOOTS.get());

        // Tools
        addItemChain(ModItems.COPPER_SWORD.get(), ModItems.EXPOSED_COPPER_SWORD.get(), ModItems.WEATHERED_COPPER_SWORD.get(), ModItems.OXIDIZED_COPPER_SWORD.get());
        addItemChain(ModItems.COPPER_SHOVEL.get(), ModItems.EXPOSED_COPPER_SHOVEL.get(), ModItems.WEATHERED_COPPER_SHOVEL.get(), ModItems.OXIDIZED_COPPER_SHOVEL.get());
        addItemChain(ModItems.COPPER_PICKAXE.get(), ModItems.EXPOSED_COPPER_PICKAXE.get(), ModItems.WEATHERED_COPPER_PICKAXE.get(), ModItems.OXIDIZED_COPPER_PICKAXE.get());
        addItemChain(ModItems.COPPER_AXE.get(), ModItems.EXPOSED_COPPER_AXE.get(), ModItems.WEATHERED_COPPER_AXE.get(), ModItems.OXIDIZED_COPPER_AXE.get());
        addItemChain(ModItems.COPPER_HOE.get(), ModItems.EXPOSED_COPPER_HOE.get(), ModItems.WEATHERED_COPPER_HOE.get(), ModItems.OXIDIZED_COPPER_HOE.get());
        addItemChain(ModItems.COPPER_SHEARS.get(), ModItems.EXPOSED_COPPER_SHEARS.get(), ModItems.WEATHERED_COPPER_SHEARS.get(), ModItems.OXIDIZED_COPPER_SHEARS.get());
    }

    private static Item getItem(String id) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static void addChainFromMap(Map<String, ? extends Supplier<? extends Block>> map, String[] states) {
        for (int i = 0; i < states.length - 1; i++) {
            Supplier<? extends Block> original = map.get(states[i]);
            Supplier<? extends Block> next = map.get(states[i+1]);
            if (original != null && next != null && original.get() != null && next.get() != null) {
                addWeatheringTransition(original.get(), next.get());
            }
        }
        for (String state : states) {
            Supplier<? extends Block> original = map.get(state);
            Supplier<? extends Block> waxed = map.get("waxed_" + state);
            if (original != null && waxed != null && original.get() != null && waxed.get() != null) {
                addWaxingTransition(original.get(), waxed.get());
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
            return Optional.of(nextStack.transmuteCopy(nextItem, stack.getCount()));
        }
        return Optional.empty();
    }

    public static Optional<ItemStack> getWaxedItem(ItemStack stack) {
        Item waxedItem = WAX_ON_BY_ITEM.get(stack.getItem());
        if (waxedItem != null) {
            ItemStack nextStack = stack.copy();
            return Optional.of(nextStack.transmuteCopy(waxedItem, stack.getCount()));
        }
        return Optional.empty();
    }
}