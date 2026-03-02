package com.otterly76.ott.block.custom;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.client.sound.AmbientDesertBlockSoundsPlayer;
import com.otterly76.ott.util.block.SpreadableBonemealableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class TallDryGrassBlock extends DeadBushBlock implements SpreadableBonemealableBlock {
    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public TallDryGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        AmbientDesertBlockSoundsPlayer.playAmbientDryGrassSounds(level, pos, random);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return SpreadableBonemealableBlock.hasSpreadableNeighbourPos(level, pos, ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState());
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        SpreadableBonemealableBlock.findSpreadableNeighbourPos(level, pos, ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState()).ifPresent((newPos) -> level.setBlockAndUpdate(newPos, ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState()));
    }
}
