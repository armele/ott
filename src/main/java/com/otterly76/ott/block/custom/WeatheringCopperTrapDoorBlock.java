package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperTrapDoorBlock extends TrapDoorBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperTrapDoorBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperTrapDoorBlock::getAge),
                            BlockSetType.CODEC.fieldOf("block_set_type").forGetter(WeatheringCopperTrapDoorBlock::getType),
                            propertiesCodec()
                    )
                    .apply(instance, WeatheringCopperTrapDoorBlock::new)
    );

    private final WeatherState weatherState;

    public WeatheringCopperTrapDoorBlock(WeatherState weatherState, BlockSetType blockSetType, Properties properties) {
        super(blockSetType, properties);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull MapCodec<? extends TrapDoorBlock> codec() {
        return CODEC;
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