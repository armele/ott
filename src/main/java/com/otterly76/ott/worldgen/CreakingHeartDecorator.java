package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.util.block.CreakingHeartState;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

public class CreakingHeartDecorator extends TreeDecorator {
    public static final MapCodec<CreakingHeartDecorator> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((creakingHeartDecorator) -> creakingHeartDecorator.probability)).apply(instance, CreakingHeartDecorator::new));
    private final float probability;

    public CreakingHeartDecorator(float f) {
        this.probability = f;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.CREAKING_HEART.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource randomSource = context.random();
        if (randomSource.nextFloat() < this.probability) {
            List<BlockPos> list = context.logs();
            if (!list.isEmpty()) {
                Set<BlockPos> logsSet = new HashSet<>(list);
                boolean bl = false;

                for(BlockPos blockPos : list) {
                    if (logsSet.contains(blockPos.above()) && logsSet.contains(blockPos.below())) {
                        for(Direction direction : Direction.values()) {
                            if (direction.getAxis().isHorizontal()) {
                                BlockPos blockPos2 = blockPos.relative(direction);
                                if (context.isAir(blockPos2) && context.isAir(blockPos.relative(direction.getOpposite()))) {
                                    context.setBlock(blockPos, ModBlocks.CREAKING_HEART.get().defaultBlockState()
                                        .setValue(CreakingHeartBlock.STATE, CreakingHeartState.DORMANT)
                                        .setValue(CreakingHeartBlock.AXIS, Direction.Axis.Y)
                                        .setValue(CreakingHeartBlock.NATURAL, true));
                                    bl = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (bl) {
                        break;
                    }
                }

            }
        }
    }
}
