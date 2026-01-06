package com.otterly76.ott;

import com.otterly76.ott.network.ClientboundSyncNutritionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ServerGameEvents {

    @SubscribeEvent
    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (!event.getEntity().getType().is(Ott.TRAMPLING_ENTITIES)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new ClientboundSyncNutritionPacket(
                    serverPlayer.getFoodData().getSaturationLevel(),
                    serverPlayer.getFoodData().getExhaustionLevel()
            ));
        }
    }
}