package com.otterly76.ott.handler;

import com.google.common.collect.MapMaker;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class BlockConversionHandler {
    public static final Component INVALID_BLOCK_COMPONENT = Component.translatable("container.invalidBlock");
    private static final java.util.Map<BlockState, BlockState> BLOCK_STATE_CONVERSIONS_CACHE = (new MapMaker()).weakKeys().weakValues().makeMap();

    public static void revertAll() {
        BLOCK_STATE_CONVERSIONS_CACHE.clear();
    }
}
