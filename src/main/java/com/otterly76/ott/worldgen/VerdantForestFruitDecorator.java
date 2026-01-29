package com.otterly76.ott.worldgen;


import com.otterly76.ott.neoforge.impl.registry.ModTreeDecoratorTypes;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VerdantForestFruitDecorator extends CocoaDecorator {
    public static final MapCodec<VerdantForestFruitDecorator> CODEC = MapCodec.unit(VerdantForestFruitDecorator::new);

    public VerdantForestFruitDecorator() {
        super(0.2F);
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.VERDANT_FRUIT.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        if (!(random.nextFloat() >= 0.2F)) {
            List<BlockPos> logs = context.logs();
            int minY = logs.getFirst().getY();
            logs.stream().filter((pos) -> pos.getY() - minY <= 2).forEach((pos) -> {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (random.nextFloat() <= 0.25F) {
                        Direction opposite = direction.getOpposite();
                        BlockPos fruitPos = pos.offset(opposite.getStepX(), 0, opposite.getStepZ());
                        if (context.isAir(fruitPos)) {
                            context.setBlock(fruitPos, oriented(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), opposite));
                        }
                    }
                }
            });
        }
    }

    private static BlockState oriented(BlockState state, Direction direction) {
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        } else if (state.hasProperty(BlockStateProperties.FACING)) {
            return state.setValue(BlockStateProperties.FACING, direction);
        }
        return state;
    }
}
