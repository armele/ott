package com.otterly76.ott.event;

import net.minecraft.client.player.RemotePlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = "ott", value = Dist.CLIENT)
public class EatingAnimationTicker {

    public static float animationTicks = 0;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof RemotePlayer player) {
            if (player.getTicksUsingItem() > 31) {
                if (animationTicks < 31) {
                    animationTicks++;
                } else {
                    animationTicks = 0;
                }
            }
        }
    }
}
