package com.otterly76.ott.util.lantern;

import net.minecraft.core.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LanternManager {
    private static final Map<BlockPos, Integer> ACTIVE_LANTERNS = new ConcurrentHashMap<>();

    public static void addLantern(BlockPos pos, int range) {
        ACTIVE_LANTERNS.put(pos, range);
    }

    public static void removeLantern(BlockPos pos) {
        ACTIVE_LANTERNS.remove(pos);
    }

    public static Map<BlockPos, Integer> getRawData() {
        return ACTIVE_LANTERNS;
    }

    public static void clear() {
        ACTIVE_LANTERNS.clear();
    }

    public static boolean isPosProtected(BlockPos pos) {
        for (Map.Entry<BlockPos, Integer> entry : ACTIVE_LANTERNS.entrySet()) {
            if (pos.closerThan(entry.getKey(), entry.getValue())) {
                return true;
            }
        }
        return false;
    }
}