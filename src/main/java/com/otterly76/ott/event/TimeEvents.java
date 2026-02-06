package com.otterly76.ott.event;

import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class TimeEvents {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
            // Respect the doDaylightCycle gamerule
            if (!level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DAYLIGHT)) {
                return;
            }

            long time = level.getDayTime();
            long dayTime = time % 24000;

            // Day: 0 to 12500
            // Night: 13000 to 23000
            boolean isDay = dayTime < 12500;
            boolean isNight = dayTime > 13000 && dayTime < 23000;

            if (isDay) {
                handleTimeScaling(level, time, OttConfig.TIME.DAY_LENGTH_MULTIPLIER.get());
            } else if (isNight) {
                handleTimeScaling(level, time, OttConfig.TIME.NIGHT_LENGTH_MULTIPLIER.get());
            }
        }
    }

    private static void handleTimeScaling(ServerLevel level, long currentTime, double multiplier) {
        if (multiplier == 1.0) return;

        if (multiplier > 1.0) {
            // Slow down time: We need to "undo" the tick advance periodically.
            double interval = multiplier / (multiplier - 1.0);
            if (level.getGameTime() % interval < 1.0) {
                level.setDayTime(currentTime - 1);
            }
        } else {
            // Speed up time: We need to add extra ticks.
            double extraTicksPending = (1.0 / multiplier) - 1.0;
            int ticksToAdd = (int) extraTicksPending;
            double fractionalTick = extraTicksPending - ticksToAdd;

            long newTime = currentTime + ticksToAdd;

            if (fractionalTick > 0 && (level.getGameTime() % (1.0 / fractionalTick) < 1.0)) {
                newTime++;
            }

            if (newTime != currentTime) {
                level.setDayTime(newTime);
            }
        }
    }
}
