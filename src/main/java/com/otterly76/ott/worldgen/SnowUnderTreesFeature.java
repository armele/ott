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
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int startX = origin.getX() + x;
                int startZ = origin.getZ() + z;

                // Find ground level ignoring leaves
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, startX, startZ);
                mutablePos.set(startX, y, startZ);
                belowPos.set(startX, y - 1, startZ);

                if (level.getBiome(mutablePos).value().coldEnoughToSnow(mutablePos)) {
                    BlockState ground = level.getBlockState(belowPos);
                    BlockState current = level.getBlockState(mutablePos);

                    // If there's air here and solid ground below, place snow
                    if (current.isAir() && ground.isFaceSturdy(level, belowPos, Direction.UP)) {
                        level.setBlock(mutablePos, Blocks.SNOW.defaultBlockState(), 2);

                        // Also force the ground block to its SNOWY state (for the white grass look)
                        if (ground.hasProperty(SnowyDirtBlock.SNOWY)) {
                            level.setBlock(belowPos, ground.setValue(SnowyDirtBlock.SNOWY, true), 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}