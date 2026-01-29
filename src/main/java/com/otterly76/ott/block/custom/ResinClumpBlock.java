package com.otterly76.ott.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.GlowLichenBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class ResinClumpBlock extends GlowLichenBlock {
    public ResinClumpBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public boolean isValidBonemealTarget(@NotNull LevelReader p_256569_, @NotNull BlockPos p_153290_, @NotNull BlockState p_153291_) {
        return false;
    }

    protected boolean propagatesSkylightDown(@NotNull BlockState p_181225_, @NotNull BlockGetter p_181226_, @NotNull BlockPos p_181227_) {
        return false;
    }
}
