package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.block.entity.CopperChestBlockEntity;
import com.otterly76.ott.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CopperChestBlock extends ChestBlock implements WeatheringCopper {
    public static final MapCodec<CopperChestBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperChestBlock::getAge),
                            propertiesCodec()
                    )
                    .apply(instance, CopperChestBlock::new)
    );

    private final WeatheringCopper.WeatherState weatherState;

    public CopperChestBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(properties, ModBlockEntities.COPPER_CHEST::get);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull MapCodec<? extends CopperChestBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CopperChestBlockEntity(pos, state);
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }
}