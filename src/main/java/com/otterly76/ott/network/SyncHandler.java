package com.otterly76.ott.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyncHandler {
    private static final Map<UUID, Float> lastSaturationLevels = new HashMap<>();
    private static final Map<UUID, Float> lastExhaustionLevels = new HashMap<>();

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("hunger").versioned("1.0.0").optional();
        registrar.playToClient(MessageExhaustionSync.TYPE, MessageExhaustionSync.CODEC, MessageExhaustionSync::handle);
        registrar.playToClient(MessageSaturationSync.TYPE, MessageSaturationSync.CODEC, MessageSaturationSync::handle);
        NeoForge.EVENT_BUS.register(new SyncHandler());
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;

        if (event.getEntity() instanceof ServerPlayer player) {
            float currentExhaustion = player.getFoodData().getExhaustionLevel();

            float currentSaturation = player.getFoodData().getSaturationLevel();
            Float lastSaturation = lastSaturationLevels.get(player.getUUID());

            if (lastSaturation == null || lastSaturation != currentSaturation) {
                PacketDistributor.sendToPlayer(player, new MessageSaturationSync(currentSaturation));
                lastSaturationLevels.put(player.getUUID(), currentSaturation);
            }

            Float lastExhaustion = lastExhaustionLevels.get(player.getUUID());

            if (lastExhaustion == null || Math.abs(lastExhaustion - currentExhaustion) >= 0.01F) {
                PacketDistributor.sendToPlayer(player, new MessageExhaustionSync(currentExhaustion));
                lastExhaustionLevels.put(player.getUUID(), currentExhaustion);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            lastSaturationLevels.remove(event.getEntity().getUUID());
            lastExhaustionLevels.remove(event.getEntity().getUUID());
        }
    }
}