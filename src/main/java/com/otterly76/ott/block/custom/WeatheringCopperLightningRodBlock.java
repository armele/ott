package com.otterly76.ott.block.custom;

import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperLightningRodBlock extends LightningRodBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public WeatheringCopperLightningRodBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return weatherState;
    }
}