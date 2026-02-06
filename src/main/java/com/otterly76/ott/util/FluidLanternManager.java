package com.otterly76.ott.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FluidLanternManager {
    private static final Map<BlockPos, Integer> WATER_LANTERNS = new ConcurrentHashMap<>();
    private static final Map<BlockPos, Integer> LAVA_LANTERNS = new ConcurrentHashMap<>();

    public static void addWaterLantern(BlockPos pos, int radius) {
        WATER_LANTERNS.put(pos, radius);
    }

    public static void addLavaLantern(BlockPos pos, int radius) {
        LAVA_LANTERNS.put(pos, radius);
    }

    public static void removeLantern(BlockPos pos) {
        WATER_LANTERNS.remove(pos);
        LAVA_LANTERNS.remove(pos);
    }

    public static Map<BlockPos, Integer> getWaterLanterns() {
        return WATER_LANTERNS;
    }

    public static Map<BlockPos, Integer> getLavaLanterns() {
        return LAVA_LANTERNS;
    }

    public static boolean isProtected(FluidState state, BlockPos pos) {
        Fluid fluid = state.getType();
        if (fluid.isSame(Fluids.WATER) || fluid.isSame(Fluids.FLOWING_WATER)) {
            return isWaterProtected(pos);
        } else if (fluid.isSame(Fluids.LAVA) || fluid.isSame(Fluids.FLOWING_LAVA)) {
            return isLavaProtected(pos);
        }
        return false;
    }

    public static boolean isWaterProtected(BlockPos pos) {
        return isPosInRange(pos, WATER_LANTERNS);
    }

    public static boolean isLavaProtected(BlockPos pos) {
        return isPosInRange(pos, LAVA_LANTERNS);
    }

    private static boolean isPosInRange(BlockPos pos, Map<BlockPos, Integer> lanterns) {
        for (Map.Entry<BlockPos, Integer> entry : lanterns.entrySet()) {
            BlockPos lanternPos = entry.getKey();
            int radius = entry.getValue();
            if (pos.closerThan(lanternPos, radius)) {
                return true;
            }
        }
        return false;
    }
}
