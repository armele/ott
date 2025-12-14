package com.otterly76.ott.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResinTrunkDecorator extends TreeDecorator {
    public static final MapCodec<ResinTrunkDecorator> CODEC = Codec.floatRange(0.0F, 1.0F)
            .fieldOf("probability")
            .xmap(ResinTrunkDecorator::new, d -> d.probability);

    private final float probability;

    public ResinTrunkDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.RESIN_TRUNK.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        WorldGenLevel level = (WorldGenLevel) context.level();

        List<BlockPos> logs = context.logs();
        if (logs.isEmpty()) return;

        for (BlockPos logPos : logs) {
            for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
                if (random.nextFloat() >= probability) continue;

                BlockPos targetPos = logPos.relative(dir);
                if (!level.getBlockState(targetPos).isAir()) continue;

                Direction face = dir.getOpposite();
                BlockState state = ModBlocks.RESIN_CLUMP.get().defaultBlockState()
                        .setValue(MultifaceBlock.getFaceProperty(face), true);

                level.setBlock(targetPos, state, 3);
            }
        }
    }
}