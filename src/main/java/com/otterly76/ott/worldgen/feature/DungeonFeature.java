package com.otterly76.ott.worldgen.feature;


import com.otterly76.ott.worldgen.feature.config.DungeonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

import java.util.function.Predicate;

public class DungeonFeature extends Feature<DungeonConfig> {
    public static final DungeonFeature FEATURE = new DungeonFeature();

    public DungeonFeature() {
        super(DungeonConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<DungeonConfig> context) {
        BlockPos startPos = context.origin();
        RandomSource random = context.random();
        WorldGenLevel world = context.level();
        DungeonConfig config = context.config();

        // Fixed Predicate logic with explicit typing
        Predicate<BlockState> predicate = config.dungeonInvalidBlocks()
                .<Predicate<BlockState>>map(set -> state -> state.is(set))
                .orElse(state -> state.is(BlockTags.FEATURES_CANNOT_REPLACE));

        int xRadius = config.radius().sample(random);
        int minX = -xRadius - 1;
        int maxX = xRadius + 1;
        int zRadius = config.radius().sample(random);
        int minZ = -zRadius - 1;
        int maxZ = zRadius + 1;
        int openings = 0;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = -1; y <= 4; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockPos currentPos = startPos.offset(x, y, z);
                    boolean isSolid = world.getBlockState(currentPos).isSolidRender(world, currentPos);
                    if (y == -1 && !isSolid) return false;
                    if (y == 4 && !isSolid) return false;

                    if ((x == minX || x == maxX || z == minZ || z == maxZ) && y == 0 && world.isEmptyBlock(currentPos) && world.isEmptyBlock(currentPos.above())) {
                        ++openings;
                    }
                }
            }
        }

        if (openings >= config.minOpenings() && openings <= config.maxOpenings()) {
            for (int x = minX; x <= maxX; ++x) {
                for (int y = 3; y >= -1; --y) {
                    for (int z = minZ; z <= maxZ; ++z) {
                        BlockPos currentPos = startPos.offset(x, y, z);
                        BlockState currentState = world.getBlockState(currentPos);
                        if (x != minX && y != -1 && z != minZ && x != maxX && y != 4 && z != maxZ) {
                            if (!currentState.is(Blocks.CHEST) && !currentState.is(Blocks.SPAWNER)) {
                                this.safeSetBlock(world, currentPos, Blocks.CAVE_AIR.defaultBlockState(), predicate);
                            }
                        } else if (currentPos.getY() >= world.getMinBuildHeight() && !world.getBlockState(currentPos.below()).isSolidRender(world, currentPos.below())) {
                            world.setBlock(currentPos, Blocks.CAVE_AIR.defaultBlockState(), 2);
                        } else if (currentState.isSolidRender(world, currentPos) && !currentState.is(Blocks.CHEST)) {
                            this.safeSetBlock(world, currentPos, y == -1 ? config.floorProvider().getState(random, currentPos) : config.wallProvider().getState(random, currentPos), predicate);
                        }
                    }
                }
            }

            for (int i = 0; i < config.maxChests(); ++i) {
                for (int attempt = 0; attempt < 3; ++attempt) {
                    int x = startPos.getX() + random.nextInt(xRadius * 2 + 1) - xRadius;
                    int y = startPos.getY();
                    int z = startPos.getZ() + random.nextInt(zRadius * 2 + 1) - zRadius;
                    BlockPos chestPos = new BlockPos(x, y, z);
                    if (world.isEmptyBlock(chestPos)) {
                        int solidFaces = 0;
                        for (Direction direction : Plane.HORIZONTAL) {
                            if (world.getBlockState(chestPos.relative(direction)).isSolidRender(world, chestPos.relative(direction))) {
                                ++solidFaces;
                            }
                        }

                        if (solidFaces == 1) {
                            this.safeSetBlock(world, chestPos, StructurePiece.reorient(world, chestPos, Blocks.CHEST.defaultBlockState()), predicate);
                            world.getBlockEntity(chestPos, BlockEntityType.CHEST).ifPresent(chest -> chest.setLootTable(config.lootTable(), random.nextLong()));
                            break;
                        }
                    }
                }
            }

            this.safeSetBlock(world, startPos, Blocks.SPAWNER.defaultBlockState(), predicate);
            BlockEntity blockEntity = world.getBlockEntity(startPos);
            if (blockEntity instanceof SpawnerBlockEntity spawner) {
                com.otterly76.ott.util.weighted.WeightedList<EntityType<?>> mobs =
                        config.spawnerMobs();

                mobs.getRandom(random).ifPresent(mobType -> spawner.setEntityId(mobType, random));
            }

            return true;
        } else {
            return false;
        }
    }
}





