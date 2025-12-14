package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

public class PaleHangingMossFromLeavesDecorator extends TreeDecorator {
    public static final MapCodec<PaleHangingMossFromLeavesDecorator> CODEC = Codec.floatRange(0.0F, 1.0F)
            .fieldOf("probability")
            .xmap(PaleHangingMossFromLeavesDecorator::new, d -> d.probability);

    private final float probability;

    public PaleHangingMossFromLeavesDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.PALE_HANGING_MOSS_FROM_LEAVES.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        WorldGenLevel level = (WorldGenLevel) context.level();

        for (BlockPos leafPos : context.leaves()) {
            if (random.nextFloat() >= probability) continue;

            BlockPos below = leafPos.below();
            PaleMossPlacement.placeHangingMossColumn(level, random, below);
        }
    }
}