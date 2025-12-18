package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PaleMossOnStuffDecorator extends TreeDecorator {
    public static final MapCodec<PaleMossOnStuffDecorator> CODEC = Codec.floatRange(0.0F, 1.0F)
            .fieldOf("probability")
            .xmap(PaleMossOnStuffDecorator::new, d -> d.probability);

    private final float probability;

    public PaleMossOnStuffDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.PALE_MOSS_ON_STUFF.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        WorldGenLevel level = (WorldGenLevel) context.level();

        List<BlockPos> logs = context.logs();
        if (logs.isEmpty()) return;

        // 1. Place moss on the logs themselves
        for (BlockPos logPos : logs) {
            if (random.nextFloat() >= probability) continue;

            BlockPos target = logPos.above();
            if (!level.isEmptyBlock(target)) continue;

            BlockState below = level.getBlockState(logPos);
            if (!below.isFaceSturdy(level, logPos, Direction.UP)) continue;

            context.setBlock(target, ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState());
        }

        // 2. Place moss on the ground around the base of the tree
        BlockPos base = findLowest(logs);
        if (base == null) return;

        int radius = 3;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (random.nextFloat() >= probability) continue;

                BlockPos start = base.offset(dx, 0, dz);
                BlockPos surface = findSurface(level, start, 8);

                if (surface != null) {
                    BlockPos target = surface.above();
                    if (level.isEmptyBlock(target)) {
                        BlockState below = level.getBlockState(surface);
                        if (below.isFaceSturdy(level, surface, Direction.UP)) {
                            context.setBlock(target, ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    @Nullable
    private static BlockPos findLowest(List<BlockPos> positions) {
        if (positions.isEmpty()) return null;
        BlockPos lowest = positions.getFirst();
        for (BlockPos p : positions) {
            if (p.getY() < lowest.getY()) lowest = p;
        }
        return lowest;
    }

    @SuppressWarnings("SameParameterValue")
    @Nullable
    private static BlockPos findSurface(WorldGenLevel level, BlockPos start, int maxDrop) {
        BlockPos p = start;

        for (int i = 0; i < 3; i++) {
            if (level.isEmptyBlock(p)) break;
            p = p.above();
        }

        for (int i = 0; i <= maxDrop; i++) {
            if (!level.isEmptyBlock(p)) {
                return p;
            }
            p = p.below();
        }

        return null;
    }
}