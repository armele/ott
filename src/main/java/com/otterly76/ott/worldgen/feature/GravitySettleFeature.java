package com.otterly76.ott.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class GravitySettleFeature extends Feature<NoneFeatureConfiguration> {

    public GravitySettleFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        ChunkPos chunkPos = new ChunkPos(context.origin());
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();

        for (int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++) {
            for (int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++) {
                settleColumn(level, x, z, minY, maxY);
            }
        }
        return true;
    }

    /**
     * Returns true if a falling block can pass through (i.e. the given state provides no support).
     * Extends vanilla's isFree() to also include pointed dripstone, which does not override
     * canBeReplaced() but should not act as a landing surface for gravity blocks.
     */
    private static boolean canFallThrough(BlockState state) {
        return FallingBlock.isFree(state) || state.is(Blocks.POINTED_DRIPSTONE);
    }

    /**
     * Scans a single block column bottom-to-top. Any FallingBlock (sand, gravel, etc.)
     * that has only air beneath it falls to the nearest solid surface below.
     * Processing bottom-to-top handles stacked gravity blocks correctly in one pass:
     * after a lower block settles, the block above it will see the settled block as
     * a solid surface when the scan reaches it.
     */
    private void settleColumn(WorldGenLevel level, int x, int z, int minY, int maxY) {
        for (int y = minY + 1; y < maxY; y++) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(pos);

            Block block = state.getBlock();
            if (!(block instanceof FallingBlock)) continue;
            BlockState below = level.getBlockState(pos.below());
            // canFallThrough: air, fire, liquids, canBeReplaced blocks (grass, moss, carpets,
            // flowers, etc.) and explicitly pointed dripstone.
            if (!canFallThrough(below)) continue;

            // Scan downward to find the first solid surface
            int landY = y - 1;
            while (landY > minY && canFallThrough(level.getBlockState(new BlockPos(x, landY, z)))) {
                landY--;
            }
            landY++; // first air slot above the solid surface

            if (landY < y) {
                level.setBlock(new BlockPos(x, landY, z), state, 3);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }
}
