package com.otterly76.ott.worldgen;


import com.otterly76.ott.api.registry.ModConfiguredFeatures;
import com.otterly76.ott.neoforge.impl.registry.ModTreeDecoratorTypes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.neoforge.impl.registry.ModBlocks;
import com.otterly76.ott.block.custom.HangingMossBlock;
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

    private static void addMossHanger(BlockPos blockPos, TreeDecorator.Context context) {
        while(context.isAir(blockPos.below()) && !((double)context.random().nextFloat() < (double)0.5F)) {
            context.setBlock(blockPos, ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(HangingMossBlock.TIP, false));
            blockPos = blockPos.below();
        }

        context.setBlock(blockPos, ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(HangingMossBlock.TIP, true));
    }

    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.PALE_MOSS.get();
    }

    public void place(TreeDecorator.Context context) {
        RandomSource randomSource = context.random();
        WorldGenLevel worldGenLevel = (WorldGenLevel)context.level();
        List<BlockPos> list = Util.shuffledCopy(context.logs(), randomSource);
        if (!list.isEmpty()) {
            Mutable<BlockPos> mutable = new MutableObject<>(list.getFirst());
            list.forEach((blockPosx) -> {
                if (blockPosx.getY() < mutable.getValue().getY()) {
                    mutable.setValue(blockPosx);
                }

            });
            BlockPos blockPos = mutable.getValue();
            if (randomSource.nextFloat() < this.groundProbability) {
                worldGenLevel.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((registry) -> registry.get(ModConfiguredFeatures.PALE_MOSS_PATCH)).ifPresent((reference) -> reference.value().place(worldGenLevel, worldGenLevel.getLevel().getChunkSource().getGenerator(), randomSource, blockPos.above()));
            }

            context.logs().forEach((blockPosx) -> {
                if (randomSource.nextFloat() < this.trunkProbability) {
                    BlockPos blockPos2 = blockPosx.below();
                    if (context.isAir(blockPos2)) {
                        addMossHanger(blockPos2, context);
                    }
                }

            });
            context.leaves().forEach((blockPosx) -> {
                if (randomSource.nextFloat() < this.leavesProbability) {
                    BlockPos blockPos2 = blockPosx.below();
                    if (context.isAir(blockPos2)) {
                        addMossHanger(blockPos2, context);
                    }
                }

            });
        }

    }
}




