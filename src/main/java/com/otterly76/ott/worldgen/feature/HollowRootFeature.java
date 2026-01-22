package com.otterly76.ott.worldgen.feature;

import com.mojang.serialization.Codec;
import com.otterly76.ott.worldgen.feature.config.HollowRootConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

import java.util.HashSet;
import java.util.Set;

public class HollowRootFeature extends Feature<HollowRootConfig> {
    public HollowRootFeature(Codec<HollowRootConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<HollowRootConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        HollowRootConfig config = context.config();
        ChunkGenerator generator = context.chunkGenerator();
        ImprovedNoise noise = new ImprovedNoise(random);

        int rawRadius = config.radius().sample(random);
        int height = config.height().sample(random);
        int span = config.span().sample(random);
        int depth = config.groundDepth().sample(random);
        float frequency = config.noiseFrequency().sample(random);
        float wallThickness = config.thickness().sample(random);

        Set<BlockPos> placedPositions = new HashSet<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        // RANDOM ORIENTATION & TWIST
        double angle = random.nextDouble() * Math.PI * 2.0;
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        double wiggleFreq = 1.2 + random.nextDouble() * 1.5;
        double wiggleAmp = 3.0 + random.nextDouble() * 5.0;

        // HIGH DENSITY SAMPLING (4 steps per block)
        int steps = span > 0 ? span * 4 : (height + depth) * 4;

        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps;
            double xOff, yOff, zOff, currentRadius;

            double twist = Math.sin(t * Math.PI * wiggleFreq) * wiggleAmp;

            if (span > 0) {
                // ARCH MATH
                double progress = t * span;
                xOff = (progress * cosA) + (twist * -sinA);
                zOff = (progress * sinA) + (twist * cosA);
                yOff = (height + depth) * 4 * t * (1 - t) - depth;
                currentRadius = rawRadius * (0.4 + 0.6 * Math.sin(t * Math.PI));
            } else {
                // SPIKE MATH
                xOff = twist * cosA;
                zOff = twist * sinA;
                yOff = (t * (height + depth)) - depth;
                currentRadius = rawRadius * (1.0 - (t * 0.6));
            }

            currentRadius = Math.max(currentRadius, 4.0);
            BlockPos center = origin.offset((int)xOff, (int)yOff, (int)zOff);
            int checkR = (int)Math.ceil(currentRadius) + 1;

            for (int x = -checkR; x <= checkR; x++) {
                for (int y = -checkR; y <= checkR; y++) {
                    for (int z = -checkR; z <= checkR; z++) {
                        mutable.set(center.getX() + x, center.getY() + y, center.getZ() + z);

                        double distSq = x * x + y * y + z * z;
                        double n = (noise.noise(mutable.getX() * frequency, mutable.getY() * frequency, mutable.getZ() * frequency) + 1.0) * 0.5;
                        double noisyRadius = Mth.lerp(n, currentRadius * 0.9, currentRadius);

                        if (distSq <= noisyRadius * noisyRadius) {
                            double innerR = noisyRadius * (1.0 - wallThickness);

                            // CORE: Hollow/Water (Clears existing blocks)
                            if (distSq < innerR * innerR) {
                                BlockState hollowState = (mutable.getY() < origin.getY()) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                                level.setBlock(mutable, hollowState, 2);
                            }
                            // SHELL: Wood/Moss/Clay
                            else {
                                if (placedPositions.add(mutable.immutable())) {
                                    BlockState state = random.nextFloat() < 0.25F ? config.decorationProvider().getState(random, mutable) : config.blockProvider().getState(random, mutable);
                                    level.setBlock(mutable, state, 2);
                                    decorateSurface(level, generator, mutable.immutable(), random, config);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void decorateSurface(WorldGenLevel level, ChunkGenerator generator, BlockPos pos, RandomSource random, HollowRootConfig config) {
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);
            if (level.isEmptyBlock(neighbor)) {
                if (dir == Direction.UP) {
                    float chance = random.nextFloat();
                    if (chance < 0.03F) {
                        config.surfaceFeature().value().place(level, generator, random, neighbor);
                    } else if (chance < 0.12F) {
                        BlockState sapling = random.nextBoolean() ? Blocks.AZALEA.defaultBlockState() : Blocks.FLOWERING_AZALEA.defaultBlockState();
                        level.setBlock(neighbor, sapling, 2);
                    } else if (chance < 0.35F) {
                        level.setBlock(neighbor, Blocks.MOSS_CARPET.defaultBlockState(), 2);
                    }
                } else if (dir == Direction.DOWN && random.nextFloat() < 0.15F) {
                    level.setBlock(neighbor, config.hangingProvider().getState(random, neighbor), 2);
                }
            }
        }
    }
}