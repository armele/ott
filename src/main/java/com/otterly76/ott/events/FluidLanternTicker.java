package com.otterly76.ott.events;

import com.otterly76.ott.Constants;
import com.otterly76.ott.util.FluidLanternManager;
import com.otterly76.ott.util.FluidLanternUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class FluidLanternTicker {

    private static final Map<ResourceKey<Level>, Integer> SLICE_INDEX = new ConcurrentHashMap<>();
    // Process every Nth Y per tick to spread load
    private static final int Y_STRIDE = 8;

    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        final int minY = level.getMinBuildHeight();
        final int maxY = level.getMaxBuildHeight();
        final int height = Math.max(1, maxY - minY);

        int idx = SLICE_INDEX.getOrDefault(level.dimension(), 0);
        int y = minY + (idx % height);

        // Advance for next tick
        SLICE_INDEX.put(level.dimension(), (idx + Y_STRIDE) % height);

        // Water lanterns: clear water + waterlogging on this Y slice
        for (Map.Entry<net.minecraft.core.BlockPos, Integer> e : FluidLanternManager.getWaterLanterns().entrySet()) {
            FluidLanternUtils.clearWaterSlice(level, e.getKey(), e.getValue(), y);
        }
        // Lava lanterns: clear lava on this Y slice
        for (Map.Entry<net.minecraft.core.BlockPos, Integer> e : FluidLanternManager.getLavaLanterns().entrySet()) {
            FluidLanternUtils.clearLavaSlice(level, e.getKey(), e.getValue(), y);
        }
    }
}