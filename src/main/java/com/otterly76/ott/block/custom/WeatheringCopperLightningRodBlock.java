package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperLightningRodBlock extends LightningRodBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperLightningRodBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperLightningRodBlock::getAge),
                            propertiesCodec()
                    )
                    .apply(instance, WeatheringCopperLightningRodBlock::new)
    );

    private final WeatherState weatherState;

    public WeatheringCopperLightningRodBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<LightningRodBlock> codec() {
        return (MapCodec<LightningRodBlock>) (MapCodec<?>) CODEC;
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
