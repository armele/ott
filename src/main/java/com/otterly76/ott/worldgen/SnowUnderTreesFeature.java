package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SnowUnderTreesFeature extends Feature<NoneFeatureConfiguration> {
    public SnowUnderTreesFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, origin.getX(), origin.getZ());
        BlockPos mutablePos = new BlockPos(origin.getX(), y, origin.getZ());
        BlockPos belowPos = mutablePos.below();

        if (level.getBiome(mutablePos).value().coldEnoughToSnow(mutablePos)) {
            BlockState ground = level.getBlockState(belowPos);
            BlockState current = level.getBlockState(mutablePos);

            if (current.isAir() && ground.isFaceSturdy(level, belowPos, Direction.UP)) {
                level.setBlock(mutablePos, Blocks.SNOW.defaultBlockState(), 2);
                if (ground.hasProperty(SnowyDirtBlock.SNOWY)) {
                    level.setBlock(belowPos, ground.setValue(SnowyDirtBlock.SNOWY, true), 2);
                }
                return true;
            }
        }
        return false;
    }
}
