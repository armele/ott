package com.otterly76.ott.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningRodBlock.class)
public abstract class LightningRodBlockMixin extends Block implements WeatheringCopper {
    public LightningRodBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    @NotNull
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }
}
