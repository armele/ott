package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.block.entity.CopperHopperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CopperHopperBlock extends HopperBlock implements WeatheringCopper {
    public static final MapCodec<CopperHopperBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperHopperBlock::getAge),
                            propertiesCodec()
                    )
                    .apply(instance, CopperHopperBlock::new)
    );

    private final WeatheringCopper.WeatherState weatherState;

    public CopperHopperBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<HopperBlock> codec() {
        return (MapCodec<HopperBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CopperHopperBlockEntity(pos, state);
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull net.minecraft.server.level.ServerLevel level, @NotNull BlockPos pos, @NotNull net.minecraft.util.RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }
}
