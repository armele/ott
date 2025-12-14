package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GlowBerryDecorator extends TreeDecorator {
    public static final MapCodec<GlowBerryDecorator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.floatRange(0.0F, 1.0F)
                            .fieldOf("probability")
                            .forGetter(d -> d.probability),
                    Codec.intRange(1, 12)
                            .fieldOf("max_length")
                            .forGetter(d -> d.maxLength)
            ).apply(instance, GlowBerryDecorator::new)
    );

    private final float probability;
    private final int maxLength;

    public GlowBerryDecorator(float probability, int maxLength) {
        this.probability = probability;
        this.maxLength = maxLength;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.GLOW_BERRY.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        WorldGenLevel level = (WorldGenLevel) context.level();

        List<BlockPos> leaves = Util.shuffledCopy(context.leaves(), random);
        for (BlockPos leafPos : leaves) {
            if (random.nextFloat() >= this.probability) {
                continue;
            }

            BlockPos start = leafPos.below();
            if (!level.isEmptyBlock(start)) {
                continue;
            }

            placeGlowBerryVines(level, random, start, this.maxLength);
        }
    }

    private static void placeGlowBerryVines(WorldGenLevel level, RandomSource random, BlockPos start, int maxLength) {
        int length = 1 + random.nextInt(Math.max(1, maxLength));

        for (int i = 0; i < length; i++) {
            BlockPos pos = start.below(i);
            if (!level.isEmptyBlock(pos)) {
                break;
            }

            boolean isTip = (i == length - 1) || !level.isEmptyBlock(pos.below());
            BlockState state = (isTip ? Blocks.CAVE_VINES : Blocks.CAVE_VINES_PLANT)
                    .defaultBlockState()
                    .setValue(BlockStateProperties.BERRIES, true);

            level.setBlock(pos, state, 3);

            if (isTip) {
                break;
            }
        }
    }
}