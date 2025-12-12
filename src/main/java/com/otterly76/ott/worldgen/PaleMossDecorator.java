package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaleMossDecorator extends TreeDecorator {
    public static final MapCodec<PaleMossDecorator> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("leaves_probability").forGetter((paleMossDecorator) -> paleMossDecorator.leavesProbability), Codec.floatRange(0.0F, 1.0F).fieldOf("trunk_probability").forGetter((paleMossDecorator) -> paleMossDecorator.trunkProbability), Codec.floatRange(0.0F, 1.0F).fieldOf("ground_probability").forGetter((paleMossDecorator) -> paleMossDecorator.groundProbability)).apply(instance, PaleMossDecorator::new));
    private final float leavesProbability;
    private final float trunkProbability;
    private final float groundProbability;

    public PaleMossDecorator(float f, float g, float h) {
        this.leavesProbability = f;
        this.trunkProbability = g;
        this.groundProbability = h;
    }

    public static void addMossHanger(WorldGenLevel level, RandomSource random, BlockPos pos) {
        if (!level.isEmptyBlock(pos)) {
            return;
        }
        PaleMossPatchFeature.addMossHanger(level, random, pos);
    }

    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.PALE_MOSS.get();
    }

    private void tryAddHanger(List<BlockPos> positions, float probability, RandomSource randomSource, TreeDecorator.Context context) {
        positions.forEach((blockPos) -> {
            if (randomSource.nextFloat() < probability) {
                BlockPos belowPos = blockPos.below();
                addMossHanger((WorldGenLevel)context.level(), randomSource, belowPos);
            }
        });
    }

    public void place(TreeDecorator.Context context) {
        RandomSource randomSource = context.random();
        WorldGenLevel worldGenLevel = (WorldGenLevel)context.level();
        List<BlockPos> list = Util.shuffledCopy(context.logs(), randomSource);
        if (!list.isEmpty()) {
            Mutable<BlockPos> mutable = new MutableObject<>(list.getFirst());
            list.forEach((blockPos) -> {
                if (blockPos.getY() < mutable.getValue().getY()) {
                    mutable.setValue(blockPos);
                }
            });
            BlockPos blockPos = mutable.getValue();
            if (randomSource.nextFloat() < this.groundProbability) {
                worldGenLevel.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((registry) -> registry.get(ModConfiguredFeatures.PALE_MOSS_PATCH)).ifPresent((reference) -> (reference.value()).place(worldGenLevel, worldGenLevel.getLevel().getChunkSource().getGenerator(), randomSource, blockPos.above()));
            }

            // Cached for minor perf gain
            List<BlockPos> logs = context.logs();
            List<BlockPos> leaves = context.leaves();

            tryAddHanger(logs, this.trunkProbability, randomSource, context);
            tryAddHanger(leaves, this.leavesProbability, randomSource, context);
        }
    }
}