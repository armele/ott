package com.otterly76.ott.util;

import net.minecraft.core.BlockPos;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LanternManager {
    // Maps a range (e.g., 64, 128, 256) to a set of block positions
    private static final Map<Integer, Set<BlockPos>> ACTIVE_LANTERNS = new ConcurrentHashMap<>();

    public static void addLantern(BlockPos pos, int range) {
        ACTIVE_LANTERNS.computeIfAbsent(range, k -> new java.util.HashSet<>()).add(pos);
    }

    public static void removeLantern(BlockPos pos) {
        ACTIVE_LANTERNS.values().forEach(set -> set.remove(pos));
    }

    // Add this getter for the saver
    public static Map<Integer, java.util.Set<BlockPos>> getRawData() {
        return ACTIVE_LANTERNS;
    }

    public static boolean isPosProtected(BlockPos spawnPos) {
        for (Map.Entry<Integer, Set<BlockPos>> entry : ACTIVE_LANTERNS.entrySet()) {
            int range = entry.getKey();
            double rangeSq = (double) range * range;

            for (BlockPos lanternPos : entry.getValue()) {
                // distSqr is much faster than dist because it skips the square root math
                if (spawnPos.distSqr(lanternPos) <= rangeSq) {
                    return true;
                }
            }
        }
        return false;
    }
}