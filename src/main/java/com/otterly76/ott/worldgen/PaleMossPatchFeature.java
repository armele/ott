package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.HangingMossBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import org.jetbrains.annotations.NotNull;

public class PaleMossPatchFeature extends Feature<RandomPatchConfiguration> {
    public PaleMossPatchFeature(Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<RandomPatchConfiguration> context) {
        boolean placed = Feature.RANDOM_PATCH.place(context);

        if (placed) {
            WorldGenLevel level = context.level();
            BlockPos origin = context.origin();
            RandomSource random = context.random();

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos pos = origin.offset(x, 1, z);  // Above patch
                    if (random.nextFloat() < 0.3F && level.isEmptyBlock(pos)) {  // Example probability; use your decorator's values
                        addMossHanger(level, random, pos);  // Use refactored method
                    }
                }
            }
        }

        return placed;
    }

    public static void addMossHanger(WorldGenLevel level, RandomSource random, BlockPos pos) {
        while (level.isEmptyBlock(pos.below()) && random.nextFloat() < 0.5F) {
            level.setBlock(pos, ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(HangingMossBlock.TIP, false), 3);
            pos = pos.below();
        }
        level.setBlock(pos, ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(HangingMossBlock.TIP, true), 3);
    }
}