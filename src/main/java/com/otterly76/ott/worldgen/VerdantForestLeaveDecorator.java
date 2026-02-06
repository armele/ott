package com.otterly76.ott.worldgen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

public class VerdantForestLeaveDecorator extends LeaveVineDecorator {
    public static final MapCodec<VerdantForestLeaveDecorator> CODEC = MapCodec.unit(VerdantForestLeaveDecorator::new);

    public VerdantForestLeaveDecorator() {
        super(0.25F);
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.VERDANT_LEAVES.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        context.leaves().forEach((pos) -> {
            if (context.random().nextFloat() < 0.25F) {
                BlockPos west = pos.west();
                if (context.isAir(west)) addVine(west, Direction.WEST, context);
            }
            if (context.random().nextFloat() < 0.25F) {
                BlockPos east = pos.east();
                if (context.isAir(east)) addVine(east, Direction.EAST, context);
            }
            if (context.random().nextFloat() < 0.25F) {
                BlockPos north = pos.north();
                if (context.isAir(north)) addVine(north, Direction.NORTH, context);
            }
            if (context.random().nextFloat() < 0.25F) {
                BlockPos south = pos.south();
                if (context.isAir(south)) addVine(south, Direction.SOUTH, context);
            }
        });
    }

    private static void addVine(BlockPos pos, Direction direction, TreeDecorator.Context context) {
        context.setBlock(pos, Blocks.AZALEA_LEAVES.defaultBlockState());
        int length = 4;
        for (BlockPos current = pos.below(); context.isAir(current) && length > 0; --length) {
            context.setBlock(current, oriented(Blocks.AZALEA_LEAVES.defaultBlockState(), direction));
            current = current.below();
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
