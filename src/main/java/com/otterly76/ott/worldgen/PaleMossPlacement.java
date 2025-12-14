package com.otterly76.ott.worldgen;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.HangingMossBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public final class PaleMossPlacement {
    private PaleMossPlacement() {}

    public static void placeHangingMossColumn(WorldGenLevel level, RandomSource random, BlockPos startPos) {
        if (!level.isEmptyBlock(startPos)) return;

        BlockPos pos = startPos;

        while (level.isEmptyBlock(pos.below()) && random.nextFloat() < 0.5F) {
            level.setBlock(
                    pos,
                    ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(HangingMossBlock.TIP, false),
                    3
            );
            pos = pos.below();
        }

        level.setBlock(
                pos,
                ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(HangingMossBlock.TIP, true),
                3
        );
    }
}