package com.otterly76.ott.handler;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;
import java.util.Optional;

public class WeatheringHandler {
    private static final BiMap<Block, Block> NEXT_BY_STATE = HashBiMap.create();
    private static final BiMap<Block, Block> WAX_ON_BY_BLOCK = HashBiMap.create();

    public static void registerTransitions() {
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
        
        // Statues - stored in a list (alternating normal/waxed)
        for (int i = 0; i < 3; i++) {
            addWeatheringTransition(ModBlocks.COPPER_GOLEM_STATUES.get(i*2).get(), ModBlocks.COPPER_GOLEM_STATUES.get((i+1)*2).get());
        }
        for (int i = 0; i < 4; i++) {
            addWaxingTransition(ModBlocks.COPPER_GOLEM_STATUES.get(i*2).get(), ModBlocks.COPPER_GOLEM_STATUES.get(i*2+1).get());
        }
    }

    private static void addChainFromMap(Map<String, ? extends DeferredBlock<? extends Block>> map, String[] states) {
        for (int i = 0; i < states.length - 1; i++) {
            addWeatheringTransition(map.get(states[i]).get(), map.get(states[i+1]).get());
        }
        for (String state : states) {
            addWaxingTransition(map.get(state).get(), map.get("waxed_" + state).get());
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
}