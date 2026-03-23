package com.otterly76.ott.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GlassJarBlock extends Block {
    protected static final VoxelShape SHAPE = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D), Block.box(5.0D, 14.0D, 5.0D, 11.0D, 16.0D, 11.0D));

    public GlassJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }
}