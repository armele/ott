package com.otterly76.ott.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

public class CopperButtonBlock extends ButtonBlock implements WeatheringCopper {
    public static final MapCodec<CopperButtonBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperButtonBlock::getAge),
                            BlockSetType.CODEC.fieldOf("block_set_type").forGetter(block -> block.type),
                            Codec.intRange(1, 1024).fieldOf("ticks_to_stay_pressed").forGetter(block -> block.ticksToStayPressed),
                            propertiesCodec()
                    )
                    .apply(instance, CopperButtonBlock::new)
    );

    private final WeatherState weatherState;
    private final BlockSetType type;
    private final int ticksToStayPressed;

    public CopperButtonBlock(WeatherState weatherState, BlockSetType blockSetType, int ticks, Properties properties) {
        super(blockSetType, ticks, properties);
        this.weatherState = weatherState;
        this.type = blockSetType;
        this.ticksToStayPressed = ticks;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<ButtonBlock> codec() {
        return (MapCodec<ButtonBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }
}
