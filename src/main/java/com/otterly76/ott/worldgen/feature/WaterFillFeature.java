package com.otterly76.ott.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

/**
 * Post-terrain cleanup feature that fills isolated air pockets inside water bodies.
 * Removes air/cave-air blocks that have 4 or more face-adjacent water blocks by
 * converting them to water source blocks. Runs multiple passes until stable to
 * handle cascading pockets (e.g. floater-cleaner leaving air voids underwater).
 */
public class WaterFillFeature extends Feature<NoneFeatureConfiguration> {

    public WaterFillFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        ChunkPos chunk = new ChunkPos(context.origin());
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        int baseX = chunk.getMinBlockX();
        int baseZ = chunk.getMinBlockZ();

        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        boolean anyFilled = false;

        // Multi-pass: repeat until no more blocks are filled (cascading pockets)
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = minY + 1; y < maxY - 1; y++) {
                        int wx = baseX + x;
                        int wz = baseZ + z;
                        mpos.set(wx, y, wz);
                        BlockState state = level.getBlockState(mpos);

                        // Only fill air-type blocks (air or cave_air)
                        if (!state.isAir()) continue;

                        int waterNeighbors = 0;
                        waterNeighbors += isWater(level, wx + 1, y,     wz) ? 1 : 0;
                        waterNeighbors += isWater(level, wx - 1, y,     wz) ? 1 : 0;
                        waterNeighbors += isWater(level, wx,     y + 1, wz) ? 1 : 0;
                        waterNeighbors += isWater(level, wx,     y - 1, wz) ? 1 : 0;
                        waterNeighbors += isWater(level, wx,     y,     wz + 1) ? 1 : 0;
                        waterNeighbors += isWater(level, wx,     y,     wz - 1) ? 1 : 0;

                        if (waterNeighbors >= 4) {
                            level.setBlock(mpos, Blocks.WATER.defaultBlockState(), 3);
                            changed = true;
                            anyFilled = true;
                        }
                    }
                }
            }
        }
        return anyFilled;
    }

    private static boolean isWater(WorldGenLevel level, int x, int y, int z) {
        return level.getFluidState(new BlockPos(x, y, z)).getType() == Fluids.WATER;
    }
}
