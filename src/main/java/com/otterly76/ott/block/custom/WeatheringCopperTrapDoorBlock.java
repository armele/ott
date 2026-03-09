package com.otterly76.ott.block.custom;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperTrapDoorBlock extends TrapDoorBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public WeatheringCopperTrapDoorBlock(WeatherState weatherState, BlockSetType blockSetType, Properties properties) {
        super(blockSetType, properties);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return weatherState;
    }
}