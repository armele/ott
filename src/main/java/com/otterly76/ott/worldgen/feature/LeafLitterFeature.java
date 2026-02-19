package com.otterly76.ott.worldgen.feature;

import com.mojang.serialization.Codec;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.LeafLitterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import com.otterly76.ott.util.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.Heightmap;

public class LeafLitterFeature extends Feature<NoneFeatureConfiguration> {
    public LeafLitterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int xRange = 0; xRange < 16; ++xRange) {
            if (random.nextBoolean()) {
                for (int zRange = 0; zRange < 16; ++zRange) {
                    if (random.nextBoolean()) {
                        int x = origin.getX() + xRange;
                        int z = origin.getZ() + zRange;
                        mutable.set(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) - 1, z);
                        if (level.getBlockState(mutable).is(BlockTags.LEAVES) && level.getBlockState(mutable).is(ModTags.Blocks.ALLOWS_LEAF_LITTER)) {
                            mutable.set(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
                            BlockState state = ModBlocks.LEAF_LITTER.get().defaultBlockState()
                                    .setValue(LeafLitterBlock.AMOUNT, random.nextIntBetweenInclusive(1, 4))
                                    .setValue(LeafLitterBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                            if (!level.getBlockState(mutable).is(ModBlocks.LEAF_LITTER.get()) && level.getBlockState(mutable).isAir() && state.canSurvive(level, mutable)) {
                                level.setBlock(mutable, state, 1);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
