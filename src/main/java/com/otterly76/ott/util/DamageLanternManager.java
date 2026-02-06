package com.otterly76.ott.util;

import net.minecraft.core.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DamageLanternManager {
    private static final Map<BlockPos, Integer> ACTIVE = new ConcurrentHashMap<>();

    public static void add(BlockPos pos, int range) {
        ACTIVE.put(pos, range);
    }

    public static void remove(BlockPos pos) {
        ACTIVE.remove(pos);
    }

    public static Map<BlockPos, Integer> getAll() {
        return ACTIVE;
    }
}
