package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperRailBlock extends RailBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperRailBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperRailBlock::getAge),
                            propertiesCodec()
                    )
                    .apply(instance, WeatheringCopperRailBlock::new)
    );

    private final WeatherState weatherState;

    public WeatheringCopperRailBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<RailBlock> codec() {
        return (MapCodec<RailBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public @NotNull WeatherState getAge() {
        return weatherState;
    }
}