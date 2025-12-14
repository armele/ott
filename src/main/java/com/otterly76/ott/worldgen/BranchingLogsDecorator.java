package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BranchingLogsDecorator extends TreeDecorator {
    public static final MapCodec<BranchingLogsDecorator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.floatRange(0.0F, 1.0F)
                            .fieldOf("probability")
                            .forGetter(d -> d.probability),
                    Codec.intRange(1, 24)
                            .fieldOf("branches")
                            .forGetter(d -> d.branches),
                    Codec.intRange(1, 12)
                            .fieldOf("min_length")
                            .forGetter(d -> d.minLength),
                    Codec.intRange(1, 16)
                            .fieldOf("max_length")
                            .forGetter(d -> d.maxLength)
            ).apply(instance, BranchingLogsDecorator::new)
    );

    private final float probability;
    private final int branches;
    private final int minLength;
    private final int maxLength;

    public BranchingLogsDecorator(float probability, int branches, int minLength, int maxLength) {
        this.probability = probability;
        this.branches = branches;
        this.minLength = minLength;
        this.maxLength = Math.max(maxLength, minLength);
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.BRANCHING_LOGS.get();
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        if (random.nextFloat() >= this.probability) {
            return;
        }

        WorldGenLevel level = (WorldGenLevel) context.level();
        List<BlockPos> logs = context.logs();
        if (logs.isEmpty()) {
            return;
        }

        int minY = logs.stream().mapToInt(BlockPos::getY).min().orElse(0);
        int maxY = logs.stream().mapToInt(BlockPos::getY).max().orElse(0);

        int topBandStart = minY + ((maxY - minY) / 2);

        List<BlockPos> candidates = logs.stream()
                .filter(p -> p.getY() >= topBandStart)
                .toList();

        List<BlockPos> pickFrom = candidates.isEmpty() ? logs : candidates;

        List<BlockPos> shuffled = new ArrayList<>(pickFrom);
        Util.shuffle(shuffled, random);

        int targetBranches = clampBranchCount(this.branches + random.nextIntBetweenInclusive(-2, 3));

        EnumSet<Direction> unusedDirs = EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

        int placedBranches = 0;
        for (BlockPos rawStart : shuffled) {
            if (placedBranches >= targetBranches) break;

            BlockPos start = rawStart.below(random.nextIntBetweenInclusive(2, 4));

            Direction dir = pickDirection(random, unusedDirs);
            int len = random.nextIntBetweenInclusive(this.minLength, this.maxLength);

            boolean doTurn = random.nextFloat() < 0.60F;
            int firstLen = doTurn ? Math.max(2, len / 2) : len;
            int secondLen = doTurn ? (len - firstLen) : 0;
            Direction turnDir = doTurn ? randomTurn(dir, random) : dir;

            boolean placed =
                    tryPlaceBranch(level, start, dir, firstLen, random)
                            && (!doTurn || tryPlaceBranch(level, start.relative(dir, firstLen), turnDir, secondLen, random));

            if (placed) {
                placedBranches++;
            }
        }
    }

    private boolean tryPlaceBranch(WorldGenLevel level, BlockPos start, Direction dir, int length, RandomSource random) {
        if (length <= 0) return true;

        if (!level.getBlockState(start).is(BlockTags.LOGS)) {
            return false;
        }

        BlockState horizontalLog = ModBlocks.PALE_OAK_LOG.get().defaultBlockState()
                .setValue(BlockStateProperties.AXIS, dir.getAxis());

        boolean placedAny = false;
        BlockPos.MutableBlockPos cursor = start.mutable();
        BlockPos lastPlaced = null;

        for (int i = 1; i <= length; i++) {
            cursor.set(start).move(dir, i);

            BlockState target = level.getBlockState(cursor);

            if (target.is(BlockTags.LEAVES)) {
                level.setBlock(cursor, horizontalLog, 3);
                placedAny = true;
                lastPlaced = cursor.immutable();

                // Heal the canopy a bit around each placed segment to reduce exposed side faces
                puffLeaves(level, lastPlaced, random, 0.25F);
            } else if (target.is(BlockTags.LOGS)) {
                break;
            } else {
                break;
            }
        }

        if (placedAny) {
            // Stronger tufting around the branch end
            puffLeaves(level, lastPlaced, random, 0.85F);

            BlockPos tipCap = lastPlaced.relative(dir);

            // Previously: only if air. This often leaves bare tips when the cap spot isn't air.
            // We keep it conservative: place leaf if air OR leaves (i.e., harmless overwrite).
            BlockState capTarget = level.getBlockState(tipCap);
            if (capTarget.isAir() || capTarget.is(BlockTags.LEAVES)) {
                level.setBlock(tipCap, ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState(), 3);
            }

            // And a little halo above/around the tip to hide the log end better
            puffLeaves(level, tipCap, random, 0.70F);
        }

        return placedAny;
    }

    private static void puffLeaves(WorldGenLevel level, BlockPos center, RandomSource random, float chance) {
        if (center == null) return;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    if (random.nextFloat() > chance) continue;

                    BlockPos p = center.offset(dx, dy, dz);
                    if (level.getBlockState(p).isAir()) {
                        level.setBlock(p, ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    private static Direction pickDirection(RandomSource random, EnumSet<Direction> unusedDirs) {
        if (!unusedDirs.isEmpty() && random.nextFloat() < 0.85F) {
            int idx = random.nextInt(unusedDirs.size());
            int i = 0;
            for (Direction d : unusedDirs) {
                if (i++ == idx) {
                    unusedDirs.remove(d);
                    return d;
                }
            }
        }
        return Direction.Plane.HORIZONTAL.getRandomDirection(random);
    }

    private static Direction randomTurn(Direction dir, RandomSource random) {
        return switch (dir) {
            case NORTH, SOUTH -> (random.nextBoolean() ? Direction.EAST : Direction.WEST);
            case EAST, WEST -> (random.nextBoolean() ? Direction.NORTH : Direction.SOUTH);
            default -> Direction.Plane.HORIZONTAL.getRandomDirection(random);
        };
    }

    private static int clampBranchCount(int v) {
        return Math.max(1, Math.min(24, v));
    }
}