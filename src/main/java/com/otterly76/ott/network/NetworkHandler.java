package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Constants.VERSION);
        registrar.playToClient(
                ClientboundSyncNutritionPacket.TYPE,
                ClientboundSyncNutritionPacket.STREAM_CODEC,
                ClientboundSyncNutritionPacket::handle
        );
    }
}