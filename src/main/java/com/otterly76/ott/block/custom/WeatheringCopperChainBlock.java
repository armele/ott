package com.otterly76.ott.block.custom;

import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperChainBlock extends ChainBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public WeatheringCopperChainBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return weatherState;
    }
}
