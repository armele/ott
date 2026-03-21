package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperCauldronBlock extends AbstractCauldronBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperCauldronBlock::getAge),
                            CauldronInteraction.CODEC.fieldOf("interactions").forGetter(block -> block.interactions),
                            propertiesCodec()
                    )
                    .apply(instance, WeatheringCopperCauldronBlock::new)
    );

    private final WeatherState weatherState;

    public WeatheringCopperCauldronBlock(WeatherState weatherState, CauldronInteraction.InteractionMap interactions, BlockBehaviour.Properties properties) {
        super(properties, interactions);
        this.weatherState = weatherState;
    }

    @Override
    public @NotNull MapCodec<WeatheringCopperCauldronBlock> codec() {
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

    @Override
    protected void receiveStalactiteDrip(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, net.minecraft.world.level.material.@NotNull Fluid fluid) {
        if (fluid == net.minecraft.world.level.material.Fluids.WATER) {
            Block filled = com.otterly76.ott.handler.CauldronInteractionHandler.getMatchingBlock(state.getBlock(), com.otterly76.ott.block.ModBlocks.COPPER_CAULDRONS, com.otterly76.ott.block.ModBlocks.COPPER_WATER_CAULDRONS);
            BlockState blockstate = filled.defaultBlockState();
            level.setBlockAndUpdate(pos, blockstate);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
            level.levelEvent(1047, pos, 0);
        } else if (fluid == net.minecraft.world.level.material.Fluids.LAVA) {
            Block filled = com.otterly76.ott.handler.CauldronInteractionHandler.getMatchingBlock(state.getBlock(), com.otterly76.ott.block.ModBlocks.COPPER_CAULDRONS, com.otterly76.ott.block.ModBlocks.COPPER_LAVA_CAULDRONS);
            BlockState blockstate = filled.defaultBlockState();
            level.setBlockAndUpdate(pos, blockstate);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
            level.levelEvent(1048, pos, 0);
        }
    }

    @Override
    public boolean isFull(@NotNull BlockState state) {
        return false;
    }

    @Override
    public void handlePrecipitation(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Biome.@NotNull Precipitation precipitation) {
        if (shouldHandlePrecipitation(level, precipitation)) {
            if (precipitation == Biome.Precipitation.RAIN) {
                // Find matching copper water cauldron
                Block filled = com.otterly76.ott.handler.CauldronInteractionHandler.getMatchingBlock(state.getBlock(), com.otterly76.ott.block.ModBlocks.COPPER_CAULDRONS, com.otterly76.ott.block.ModBlocks.COPPER_WATER_CAULDRONS);
                level.setBlockAndUpdate(pos, filled.defaultBlockState());
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(filled.defaultBlockState()));
            } else if (precipitation == Biome.Precipitation.SNOW) {
                // Find matching copper powder snow cauldron
                Block filled = com.otterly76.ott.handler.CauldronInteractionHandler.getMatchingBlock(state.getBlock(), com.otterly76.ott.block.ModBlocks.COPPER_CAULDRONS, com.otterly76.ott.block.ModBlocks.COPPER_POWDER_SNOW_CAULDRONS);
                level.setBlockAndUpdate(pos, filled.defaultBlockState());
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(filled.defaultBlockState()));
            }
        }
    }

    public static boolean shouldHandlePrecipitation(Level level, Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN) {
            return level.getRandom().nextFloat() < 0.05F;
        } else if (precipitation == Biome.Precipitation.SNOW) {
            return level.getRandom().nextFloat() < 0.1F;
        } else {
            return false;
        }
    }
}
