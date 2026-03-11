package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

public class CopperPressurePlateBlock extends PressurePlateBlock implements WeatheringCopper {
    public static final MapCodec<CopperPressurePlateBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperPressurePlateBlock::getAge),
                            BlockSetType.CODEC.fieldOf("block_set_type").forGetter(block -> block.type),
                            propertiesCodec()
                    )
                    .apply(instance, CopperPressurePlateBlock::new)
    );

    private final WeatherState weatherState;

    public CopperPressurePlateBlock(WeatherState weatherState, BlockSetType blockSetType, Properties properties) {
        super(blockSetType, properties);
        this.weatherState = weatherState;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<PressurePlateBlock> codec() {
        return (MapCodec<PressurePlateBlock>) (MapCodec<?>) CODEC;
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