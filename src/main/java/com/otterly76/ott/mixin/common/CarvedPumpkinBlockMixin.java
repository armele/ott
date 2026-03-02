package com.otterly76.ott.mixin.common;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.CopperGolem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin {
    @Unique
    private static final Predicate<BlockState> OTT$IS_PUMPKIN = state -> 
            state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN);

    @Unique
    private static final Predicate<BlockState> OTT$IS_COPPER_BLOCK = state -> 
            state.is(Blocks.COPPER_BLOCK) || state.is(Blocks.EXPOSED_COPPER) || 
            state.is(Blocks.WEATHERED_COPPER) || state.is(Blocks.OXIDIZED_COPPER) ||
            state.is(Blocks.WAXED_COPPER_BLOCK) || state.is(Blocks.WAXED_EXPOSED_COPPER) ||
            state.is(Blocks.WAXED_WEATHERED_COPPER) || state.is(Blocks.WAXED_OXIDIZED_COPPER);

    @Inject(method = "onPlace", at = @At("TAIL"))
    private void ott$onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
        if (!oldState.is(state.getBlock())) {
            this.ott$trySpawnCopperGolem(level, pos);
        }
    }

    @Unique
    private void ott$trySpawnCopperGolem(Level level, BlockPos pos) {
        BlockState pumpkinState = level.getBlockState(pos);
        if (!OTT$IS_PUMPKIN.test(pumpkinState)) return;

        for (Direction direction : Direction.values()) {
            BlockPos copperPos = pos.relative(direction);
            BlockState copperState = level.getBlockState(copperPos);
            
            if (OTT$IS_COPPER_BLOCK.test(copperState)) {
                WeatheringCopper.WeatherState weather = ott$getWeatherStateFromBlock(copperState);
                
                if (!level.isClientSide) {
                    // 1. Clear blocks
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, pos, Block.getId(pumpkinState));
                    
                    level.setBlock(copperPos, Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, copperPos, Block.getId(copperState));
                    
                    // 2. Place Copper Chest
                    Block chestBlock = ott$getChestBlockFromWeather(weather);
                    level.setBlock(copperPos, chestBlock.defaultBlockState(), 3);
                    
                    // 3. Spawn Golem
                    CopperGolem golem = ModEntities.COPPER_GOLEM.get().create(level);
                    if (golem != null) {
                        golem.setWeatherState(weather);
                        golem.moveTo((double)pos.getX() + 0.5, (double)pos.getY() + 0.05, (double)pos.getZ() + 0.5, 0.0F, 0.0F);
                        level.addFreshEntity(golem);

                        if (level instanceof ServerLevel serverLevel) {
                            for (ServerPlayer serverplayer : serverLevel.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0))) {
                                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, golem);
                            }
                        }
                    }
                }
                return;
            }
        }
    }

    @Unique
    private Block ott$getChestBlockFromWeather(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED -> ModBlocks.COPPER_CHEST.get();
            case EXPOSED -> ModBlocks.EXPOSED_COPPER_CHEST.get();
            case WEATHERED -> ModBlocks.WEATHERED_COPPER_CHEST.get();
            case OXIDIZED -> ModBlocks.OXIDIZED_COPPER_CHEST.get();
        };
    }

    @Unique
    private WeatheringCopper.WeatherState ott$getWeatherStateFromBlock(BlockState state) {
        if (state.is(Blocks.EXPOSED_COPPER) || state.is(Blocks.WAXED_EXPOSED_COPPER)) return WeatheringCopper.WeatherState.EXPOSED;
        if (state.is(Blocks.WEATHERED_COPPER) || state.is(Blocks.WAXED_WEATHERED_COPPER)) return WeatheringCopper.WeatherState.WEATHERED;
        if (state.is(Blocks.OXIDIZED_COPPER) || state.is(Blocks.WAXED_OXIDIZED_COPPER)) return WeatheringCopper.WeatherState.OXIDIZED;
        return WeatheringCopper.WeatherState.UNAFFECTED;
    }
}
