package com.otterly76.ott.worldgen.feature;

import com.otterly76.ott.worldgen.feature.config.WellConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Plane;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Optional;

public class WellFeature extends Feature<WellConfig> {
    public static final WellFeature FEATURE = new WellFeature();

    public WellFeature() {
        super(WellConfig.CODEC);
    }

    public boolean place(FeaturePlaceContext<WellConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        WellConfig config = context.config();
        RandomSource random = context.random();

        for(int x = -2; x <= 2; ++x) {
            for(int z = -2; z <= 2; ++z) {
                if (world.isEmptyBlock(origin.offset(x, -1, z)) && world.isEmptyBlock(origin.offset(x, -2, z))) {
                    return false;
                }
            }
        }

        for(int var16 = -2; var16 <= 2; ++var16) {
            for(int y = -3; y <= 3; ++y) {
                for(int z = -2; z <= 2; ++z) {
                    BlockPos pos = origin.offset(var16, y, z);
                    boolean outer = Math.abs(var16) == 2 || Math.abs(z) == 2;
                    boolean middle = Math.abs(var16) == 1 && Math.abs(z) == 1;
                    boolean inner = var16 == 0 && z == 0;
                    boolean axisAligned = var16 == 0 || z == 0;
                    BlockStateProvider blockProvider;
                    if (y == -3) {
                        blockProvider = config.standardProvider();
                    } else if (y < 0) {
                        if (axisAligned && !outer) {
                            blockProvider = y == -2 ? config.groundProvider() : config.fluidProvider();
                        } else {
                            blockProvider = config.standardProvider();
                        }
                    } else if (outer) {
                        blockProvider = y > 0 ? BlockStateProvider.simple(Blocks.AIR) : (axisAligned ? config.slabProvider() : config.standardProvider());
                    } else if (middle && y != 3) {
                        blockProvider = config.standardProvider();
                    } else if (y == 3) {
                        blockProvider = inner ? config.standardProvider() : config.slabProvider();
                    } else {
                        blockProvider = BlockStateProvider.simple(Blocks.AIR);
                    }

                    world.setBlock(pos, blockProvider.getState(random, pos), 2);
                }
            }
        }

        for(int i = 0; i < config.suspiciousPlacements().sample(random); ++i) {
            for(int offset = 0; offset < 2; ++offset) {
                BlockPos pos = origin.below(offset + 2).relative(Plane.HORIZONTAL.getRandomDirection(random));
                world.setBlock(pos, config.suspiciousProvider().getState(random, pos), 2);
                Optional<BrushableBlockEntity> susBlock = world.getBlockEntity(pos, BlockEntityType.BRUSHABLE_BLOCK);
                susBlock.ifPresent(brushableBlockEntity -> brushableBlockEntity.setLootTable(config.suspiciousLootTable(), pos.asLong()));
            }
        }

        return true;
    }
}
