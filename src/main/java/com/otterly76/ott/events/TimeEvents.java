package com.otterly76.ott.events;

import com.otterly76.ott.Constants;
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

            // Day: 0 to 12000 (roughly)
            // Night: 13000 to 23000 (roughly)

            boolean isDay = dayTime < 12500;
            boolean isNight = dayTime > 13000 && dayTime < 23000;

            if (isDay) {
                // Double the length of day (advance every 2nd tick)
                // We "undo" the advance by setting time back by 1 every other tick
                if (level.getGameTime() % 2 == 0) {
                    level.setDayTime(time - 1);
                }
            } else if (isNight) {
                // Halve the length of night
                // We add an extra tick every tick to go twice as fast
                level.setDayTime(time + 1);
            }
        }
    }
}