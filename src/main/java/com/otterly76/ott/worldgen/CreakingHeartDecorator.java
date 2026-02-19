package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.util.block.CreakingHeartState;
import java.util.List;
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
                boolean bl = false;

                for(BlockPos blockPos : list) {
                    for(Direction direction : Direction.values()) {
                        BlockPos blockPos2 = blockPos.relative(direction);
                        if (context.isAir(blockPos2) && direction.getAxis().isHorizontal() && context.isAir(blockPos2.relative(direction.getOpposite(), 2))) {
                            context.setBlock(blockPos, ModBlocks.CREAKING_HEART.get().defaultBlockState().setValue(CreakingHeartBlock.STATE, CreakingHeartState.DORMANT).setValue(CreakingHeartBlock.AXIS, direction.getAxis()));
                            bl = true;
                            break;
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