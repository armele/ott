package com.otterly76.ott.block.custom;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperBarsBlock extends IronBarsBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public WeatheringCopperBarsBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return weatherState;
    }
}
