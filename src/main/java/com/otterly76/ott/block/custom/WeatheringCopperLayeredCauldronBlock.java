package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperLayeredCauldronBlock extends LayeredCauldronBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperLayeredCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperLayeredCauldronBlock::getAge),
                            Biome.Precipitation.CODEC.fieldOf("precipitation").forGetter(block -> block.precipitationType),
                            CauldronInteraction.CODEC.fieldOf("interactions").forGetter(block -> block.interactions),
                            propertiesCodec()
                    )
                    .apply(instance, WeatheringCopperLayeredCauldronBlock::new)
    );

    private final WeatherState weatherState;
    public final Biome.Precipitation precipitationType;

    public WeatheringCopperLayeredCauldronBlock(WeatherState weatherState, Biome.Precipitation precipitationType, CauldronInteraction.InteractionMap interactions, BlockBehaviour.Properties properties) {
        super(precipitationType, interactions, properties);
        this.weatherState = weatherState;
        this.precipitationType = precipitationType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<LayeredCauldronBlock> codec() {
        return (MapCodec<LayeredCauldronBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide && entity.isOnFire() && this.isEntityInsideContent(state, pos, entity)) {
            entity.clearFire();
            if (entity.mayInteract(level, pos)) {
                this.handleEntityOnFireInside(state, level, pos);
            }
        }
    }

    private void handleEntityOnFireInside(BlockState state, Level level, BlockPos pos) {
        if (this.precipitationType == Biome.Precipitation.SNOW) {
            // Replicate vanilla behavior for powder snow turning to water
            Block waterCauldron = com.otterly76.ott.handler.CauldronInteractionHandler.getMatchingBlock(state.getBlock(), com.otterly76.ott.block.ModBlocks.COPPER_POWDER_SNOW_CAULDRONS, com.otterly76.ott.block.ModBlocks.COPPER_WATER_CAULDRONS);
            this.lowerCopperFillLevel(waterCauldron.defaultBlockState().setValue(LEVEL, state.getValue(LEVEL)), level, pos);
        } else {
            this.lowerCopperFillLevel(state, level, pos);
        }
    }

    private void lowerCopperFillLevel(BlockState state, Level level, BlockPos pos) {
        int i = state.getValue(LEVEL) - 1;
        BlockState newState;
        if (i == 0) {
            // Find matching copper empty cauldron
            Block currentBlock = state.getBlock();
            Block emptyBlock;
            if (this.precipitationType == Biome.Precipitation.RAIN) {
                emptyBlock = com.otterly76.ott.handler.CauldronInteractionHandler.getMatchingBlock(currentBlock, com.otterly76.ott.block.ModBlocks.COPPER_WATER_CAULDRONS, com.otterly76.ott.block.ModBlocks.COPPER_CAULDRONS);
            } else {
                emptyBlock = com.otterly76.ott.handler.CauldronInteractionHandler.getMatchingBlock(currentBlock, com.otterly76.ott.block.ModBlocks.COPPER_POWDER_SNOW_CAULDRONS, com.otterly76.ott.block.ModBlocks.COPPER_CAULDRONS);
            }
            newState = emptyBlock.defaultBlockState();
        } else {
            newState = state.setValue(LEVEL, i);
        }
        level.setBlockAndUpdate(pos, newState);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
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
