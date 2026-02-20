package com.otterly76.ott.block.custom;

import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperLanternBlock extends LanternBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public WeatheringCopperLanternBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return weatherState;
    }
}