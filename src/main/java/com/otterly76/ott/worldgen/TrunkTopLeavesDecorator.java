package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrunkTopLeavesDecorator extends TreeDecorator {
    public static final MapCodec<TrunkTopLeavesDecorator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.floatRange(0.0F, 1.0F)
                            .fieldOf("probability")
                            .forGetter(d -> d.probability)
            ).apply(instance, TrunkTopLeavesDecorator::new)
    );

    private final float probability;

    public TrunkTopLeavesDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.TRUNK_TOP_LEAVES.get();
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
        if (logs.isEmpty()) return;

        Set<BlockPos> logSet = new HashSet<>(logs);

        for (BlockPos logPos : logs) {
            BlockPos above = logPos.above();

            if (logSet.contains(above)) continue;

            placeLeafIfAir(level, above);

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue;

                    BlockPos aroundTop = logPos.offset(dx, 0, dz);
                    placeLeafIfAir(level, aroundTop);

                    if (random.nextFloat() < 0.55F) {
                        placeLeafIfAir(level, aroundTop.above());
                    }
                }
            }
        }
    }

    private static void placeLeafIfAir(WorldGenLevel level, BlockPos pos) {
        if (level.getBlockState(pos).isAir()) {
            level.setBlock(pos, ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState(), 3);
        }
    }
}