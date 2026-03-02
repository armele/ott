package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AttachedToLogsDecorator extends TreeDecorator {
    public static final MapCodec<AttachedToLogsDecorator> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((decorator) -> decorator.probability), BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((decorator) -> decorator.blockProvider), ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter((decorator) -> decorator.directions)).apply(instance, AttachedToLogsDecorator::new));
    private final float probability;
    private final BlockStateProvider blockProvider;
    private final List<Direction> directions;

    public AttachedToLogsDecorator(float probability, BlockStateProvider blockProvider, List<Direction> directions) {
        this.probability = probability;
        this.blockProvider = blockProvider;
        this.directions = directions;
    }

    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();

        for(BlockPos pos : Util.shuffledCopy(context.logs(), random)) {
            Direction direction = Util.getRandom(this.directions, random);
            BlockPos offset = pos.relative(direction);
            if (random.nextFloat() <= this.probability && context.isAir(offset)) {
                context.setBlock(offset, this.blockProvider.getState(random, offset));
            }
        }

    }

    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.ATTACHED_TO_LOGS.get();
    }
}
