package com.otterly76.ott.worldgen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import org.jetbrains.annotations.NotNull;

public class VerdantForestTrunkDecorator extends TrunkVineDecorator {
    public static final MapCodec<VerdantForestTrunkDecorator> CODEC = MapCodec.unit(VerdantForestTrunkDecorator::new);

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.VERDANT_TRUNK.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        context.logs().forEach((pos) -> {
            if (context.random().nextInt(3) > 0) {
                BlockPos west = pos.west();
                if (context.isAir(west)) {
                    context.setBlock(west, oriented(Blocks.AZALEA_LEAVES.defaultBlockState(), Direction.EAST));
                }
            }

            if (context.random().nextInt(3) > 0) {
                BlockPos east = pos.east();
                if (context.isAir(east)) {
                    context.setBlock(east, oriented(Blocks.AZALEA_LEAVES.defaultBlockState(), Direction.WEST));
                }
            }

            if (context.random().nextInt(3) > 0) {
                BlockPos north = pos.north();
                if (context.isAir(north)) {
                    context.setBlock(north, oriented(Blocks.AZALEA_LEAVES.defaultBlockState(), Direction.SOUTH));
                }
            }

            if (context.random().nextInt(3) > 0) {
                BlockPos south = pos.south();
                if (context.isAir(south)) {
                    context.setBlock(south, oriented(Blocks.AZALEA_LEAVES.defaultBlockState(), Direction.NORTH));
                }
            }
        });
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
