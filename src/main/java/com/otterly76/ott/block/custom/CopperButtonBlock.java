package com.otterly76.ott.block.custom;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

public class CopperButtonBlock extends ButtonBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public CopperButtonBlock(WeatherState weatherState, BlockSetType blockSetType, int ticks, Properties properties) {
        super(blockSetType, ticks, properties);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return weatherState;
    }
}