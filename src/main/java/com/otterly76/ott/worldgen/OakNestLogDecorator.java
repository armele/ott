package com.otterly76.ott.worldgen;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.OakNestBlock;
import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OakNestLogDecorator extends TreeDecorator {
    public static final OakNestLogDecorator INSTANCE = new OakNestLogDecorator();
    public static final MapCodec<OakNestLogDecorator> CODEC = MapCodec.unit(OakNestLogDecorator::new);

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.OAK_NEST.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        List<BlockPos> logs = context.logs();
        int count = logs.size() > 6 ? 3 : 2;

        for (int i = 0; i < count; ++i) {
            this.placeOakNest(context, logs, Mth.nextInt(random, 3, logs.size() - 1), random);
        }
    }

    private boolean placeOakNest(TreeDecorator.Context context, List<BlockPos> logs, int index, RandomSource random) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos pos = logs.get(index);
        WorldGenLevel worldGenLevel = (WorldGenLevel) context.level();
        
        if (!worldGenLevel.getBlockState(pos.above()).is(ModBlocks.OAK_NEST.get()) &&
            !worldGenLevel.getBlockState(pos).is(ModBlocks.OAK_NEST.get()) && 
            context.isAir(pos.relative(direction))) {
            
            BlockState nestState = ModBlocks.OAK_NEST.get().defaultBlockState()
                    .setValue(OakNestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(context.random()));
            
            context.setBlock(pos, nestState);
            
            worldGenLevel.getBlockEntity(pos, ModBlockEntities.OAK_NEST.get()).ifPresent((blockEntity) -> {
                int count = 2 + random.nextInt(2);
                for (int j = 0; j < count; ++j) {
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(ModEntities.HOOPOE.get()).toString());
                    blockEntity.addHoopoe(nbt, random.nextInt(599));
                }
            });
            return true;
        } else {
            return index != 1 && this.placeOakNest(context, logs, index - 1, random);
        }
    }
}
