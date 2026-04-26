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

import java.util.HashMap;
import java.util.Map;

public class GravitySettleFeature extends Feature<NoneFeatureConfiguration> {

    /**
     * Maps unsupported falling blocks to their stable geological equivalents.
     * Gravel suspended over air/water becomes stone, sand becomes sandstone, etc.
     */
    private static final Map<Block, Block> CONVERSIONS = new HashMap<>();

    static {
        CONVERSIONS.put(Blocks.SAND,              Blocks.SANDSTONE);
        CONVERSIONS.put(Blocks.RED_SAND,          Blocks.RED_SANDSTONE);
        CONVERSIONS.put(Blocks.GRAVEL,            Blocks.STONE);
        CONVERSIONS.put(Blocks.SUSPICIOUS_SAND,   Blocks.SANDSTONE);
        CONVERSIONS.put(Blocks.SUSPICIOUS_GRAVEL, Blocks.STONE);
        // Concrete powders → matching hardened concrete
        CONVERSIONS.put(Blocks.WHITE_CONCRETE_POWDER,      Blocks.WHITE_CONCRETE);
        CONVERSIONS.put(Blocks.ORANGE_CONCRETE_POWDER,     Blocks.ORANGE_CONCRETE);
        CONVERSIONS.put(Blocks.MAGENTA_CONCRETE_POWDER,    Blocks.MAGENTA_CONCRETE);
        CONVERSIONS.put(Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE);
        CONVERSIONS.put(Blocks.YELLOW_CONCRETE_POWDER,     Blocks.YELLOW_CONCRETE);
        CONVERSIONS.put(Blocks.LIME_CONCRETE_POWDER,       Blocks.LIME_CONCRETE);
        CONVERSIONS.put(Blocks.PINK_CONCRETE_POWDER,       Blocks.PINK_CONCRETE);
        CONVERSIONS.put(Blocks.GRAY_CONCRETE_POWDER,       Blocks.GRAY_CONCRETE);
        CONVERSIONS.put(Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE);
        CONVERSIONS.put(Blocks.CYAN_CONCRETE_POWDER,       Blocks.CYAN_CONCRETE);
        CONVERSIONS.put(Blocks.PURPLE_CONCRETE_POWDER,     Blocks.PURPLE_CONCRETE);
        CONVERSIONS.put(Blocks.BLUE_CONCRETE_POWDER,       Blocks.BLUE_CONCRETE);
        CONVERSIONS.put(Blocks.BROWN_CONCRETE_POWDER,      Blocks.BROWN_CONCRETE);
        CONVERSIONS.put(Blocks.GREEN_CONCRETE_POWDER,      Blocks.GREEN_CONCRETE);
        CONVERSIONS.put(Blocks.RED_CONCRETE_POWDER,        Blocks.RED_CONCRETE);
        CONVERSIONS.put(Blocks.BLACK_CONCRETE_POWDER,      Blocks.BLACK_CONCRETE);
    }

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
        return FallingBlock.isFree(state)
                || state.is(Blocks.POINTED_DRIPSTONE)
                || state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.TALL_GRASS)
                || state.is(Blocks.MOSS_CARPET);
    }

    /**
     * Scans a single block column bottom-to-top. Any FallingBlock that has no solid
     * support below it (air, water, or other passable state) is converted in-place to
     * its stable geological equivalent (gravel→stone, sand→sandstone, etc.) rather than
     * being dropped. This prevents suspended blocks from threatening players while also
     * preserving the terrain silhouette — no blocks disappear or relocate.
     */
    private void settleColumn(WorldGenLevel level, int x, int z, int minY, int maxY) {
        for (int y = minY + 1; y < maxY; y++) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(pos);

            Block block = state.getBlock();
            if (!(block instanceof FallingBlock)) continue;

            Block stable = CONVERSIONS.get(block);
            if (stable == null) continue; // unrecognised falling block, leave it alone

            BlockState below = level.getBlockState(pos.below());
            if (!canFallThrough(below)) continue; // already supported, no conversion needed

            level.setBlock(pos, stable.defaultBlockState(), 3);
        }
    }
}
