package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
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
                    BlockPos pos = origin.offset(x, 1, z);
                    if (random.nextFloat() < 0.3F && level.isEmptyBlock(pos)) {
                        PaleMossPlacement.placeHangingMossColumn(level, random, pos);
                    }
                }
            }
        }

        return placed;
    }
}