package com.otterly76.ott.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public class FloodingManager {
    private static final int FLOOD_DELAY_TICKS = 10;
    private static final int MAX_DEPTH = 6;
    private static final List<ScheduledFlood> scheduledFloods = new ArrayList<>();

    public record ScheduledFlood(ServerLevel level, BlockPos pos, int depth, long targetTick) {}

    public static void scheduleFlooding(Level level, BlockPos pos, int depth) {
        if (level instanceof ServerLevel serverLevel) {
            scheduledFloods.add(new ScheduledFlood(serverLevel, pos.immutable(), depth, serverLevel.getGameTime() + FLOOD_DELAY_TICKS));
        }
    }

    public static void tick(ServerLevel level) {
        long currentTime = level.getGameTime();
        scheduledFloods.removeIf(entry -> {
            if (entry.level() == level && currentTime >= entry.targetTick()) {
                executeFlood(entry);
                return true;
            }
            return false;
        });
    }

    private static void executeFlood(ScheduledFlood entry) {
        ServerLevel level = entry.level();
        BlockPos pos = entry.pos();
        FluidState fluidState = level.getFluidState(pos);

        // Turn air or flowing water into a source block
        if (fluidState.isEmpty() || (fluidState.getType() == Fluids.FLOWING_WATER)) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 11);

            // Spread horizontally at sea level
            if (entry.depth() < MAX_DEPTH && pos.getY() >= level.getSeaLevel() - 1) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockPos neighbor = pos.relative(direction);
                    if (level.getBlockState(neighbor).isAir()) {
                        scheduleFlooding(level, neighbor, entry.depth() + 1);
                    }
                }
            }
        }
    }
}