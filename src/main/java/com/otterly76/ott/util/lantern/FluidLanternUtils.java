package com.otterly76.ott.util.lantern;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class FluidLanternUtils {
    public static void clearWater(Level level, BlockPos center, int radius) {
        clearFluid(level, center, radius, true);
    }

    public static void clearLava(Level level, BlockPos center, int radius) {
        clearFluid(level, center, radius, false);
    }

    public static void clearWaterSlice(Level level, BlockPos center, int radius, int y) {
        clearFluidSlice(level, center, radius, y, true);
    }

    public static void clearLavaSlice(Level level, BlockPos center, int radius, int y) {
        clearFluidSlice(level, center, radius, y, false);
    }

    private static void clearFluid(Level level, BlockPos center, int radius, boolean water) {
        int r = Math.max(1, radius);
        int minY = Math.max(level.getMinBuildHeight(), center.getY() - r);
        int maxY = Math.min(level.getMaxBuildHeight(), center.getY() + r + 1);

        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                BlockPos col = center.offset(dx, 0, dz);
                if (!level.isLoaded(col)) continue;
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(col.getX(), y, col.getZ());
                    tryClear(level, pos, center, radius, water);
                }
            }
        }
    }

    private static void clearFluidSlice(Level level, BlockPos center, int radius, int y, boolean water) {
        int r = Math.max(1, radius);
        if (y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) return;
        if (Math.abs(y - center.getY()) > r) return;

        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                BlockPos pos = new BlockPos(center.getX() + dx, y, center.getZ() + dz);
                if (!level.isLoaded(pos)) continue;
                tryClear(level, pos, center, radius, water);
            }
        }
    }

    private static void tryClear(Level level, BlockPos pos, BlockPos center, int radius, boolean water) {
        if (!pos.closerThan(center, radius)) return;
        BlockState state = level.getBlockState(pos);
        FluidState fs = state.getFluidState();
        if (water) {
            if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
                if (state.getValue(BlockStateProperties.WATERLOGGED)) {
                    level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, false), 3);
                }
            } else if (fs.getType() == Fluids.WATER || fs.getType() == Fluids.FLOWING_WATER) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        } else {
            if (fs.getType() == Fluids.LAVA || fs.getType() == Fluids.FLOWING_LAVA) {
                // Only clear if it's a liquid block or air-like; don't remove solid blocks (like the lantern)
                if (fs.isSource() || state.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }
}
