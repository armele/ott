package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CreakingHeartDecorator extends TreeDecorator {
    public static final MapCodec<CreakingHeartDecorator> CODEC =
            Codec.floatRange(0.0F, 1.0F)
                    .fieldOf("probability")
                    .xmap(CreakingHeartDecorator::new, (creakingHeartDecorator) -> creakingHeartDecorator.probability);

    private final float probability;

    public CreakingHeartDecorator(float f) {
        this.probability = f;
    }

    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.CREAKING_HEART.get();
    }

    public void place(TreeDecorator.Context context) {
        RandomSource randomSource = context.random();
        List<BlockPos> logs = context.logs();
        if (logs.isEmpty() || randomSource.nextFloat() >= this.probability) {
            return;
        }

        List<BlockPos> shuffled = new ArrayList<>(logs);
        Util.shuffle(shuffled, randomSource);

        Optional<BlockPos> candidate = shuffled.stream()
                .filter((pos) -> isEmbeddedInTrunk(context, pos))
                .findFirst();

        candidate.ifPresent((pos) -> context.setBlock(
                pos,
                ModBlocks.CREAKING_HEART.get().defaultBlockState()
                        .setValue(CreakingHeartBlock.AXIS, Direction.Axis.Y)
                        .setValue(CreakingHeartBlock.NATURAL, true)
                        .setValue(CreakingHeartBlock.ENABLED, true)
                        .setValue(CreakingHeartBlock.ACTIVE, true)
        ));
    }

    private boolean isEmbeddedInTrunk(TreeDecorator.Context context, BlockPos pos) {
        Predicate<BlockState> isLog = s -> s.is(BlockTags.LOGS);

        // Require vertical embedding: log above AND log below
        if (!checkBlock(context, pos.above(), isLog) || !checkBlock(context, pos.below(), isLog)) {
            return false;
        }

        int horizontalLogSides = 0;
        if (checkBlock(context, pos.north(), isLog)) horizontalLogSides++;
        if (checkBlock(context, pos.south(), isLog)) horizontalLogSides++;
        if (checkBlock(context, pos.east(), isLog)) horizontalLogSides++;
        if (checkBlock(context, pos.west(), isLog)) horizontalLogSides++;

        return horizontalLogSides >= 2;
    }

    public boolean checkBlock(TreeDecorator.Context context, BlockPos blockPos, Predicate<BlockState> predicate) {
        return context.level().isStateAtPosition(blockPos, predicate);
    }
}