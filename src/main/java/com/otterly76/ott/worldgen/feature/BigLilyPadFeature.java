package com.otterly76.ott.worldgen.feature;

import com.mojang.serialization.Codec;
import com.otterly76.ott.block.BigLilyPadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BigLilyPadFeature extends Feature<NoneFeatureConfiguration> {
    public BigLilyPadFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);

        BlockPos tl = origin.relative(facing);
        BlockPos tr = origin.relative(facing).relative(facing.getClockWise());
        BlockPos br = origin.relative(facing.getClockWise());

        if (isValidPlacement(level, origin) && isValidPlacement(level, tl) && isValidPlacement(level, tr) && isValidPlacement(level, br)) {
            BigLilyPadBlock.placeAt(level, facing, origin, 3);
            return true;
        }

        return false;
    }

    private boolean isValidPlacement(WorldGenLevel level, BlockPos pos) {
        // Must be air/replaceable and have water below
        return level.isEmptyBlock(pos) && level.getFluidState(pos.below()).is(net.minecraft.world.level.material.Fluids.WATER);
    }
}
