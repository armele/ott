package com.otterly76.ott.neoforge.impl.network;


import com.otterly76.ott.api.core.Constants;
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

        registrar.playToServer(
                ServerboundOpenTrashPacket.TYPE,
                net.minecraft.network.codec.StreamCodec.unit(new ServerboundOpenTrashPacket()),
                ServerboundOpenTrashPacket::handle
        );

        registrar.playToServer(
                ServerboundConfirmTrashPacket.TYPE,
                net.minecraft.network.codec.StreamCodec.unit(new ServerboundConfirmTrashPacket()),
                ServerboundConfirmTrashPacket::handle
        );

        registrar.playToServer(
                ServerboundTeleportHomePacket.TYPE,
                ServerboundTeleportHomePacket.STREAM_CODEC,
                ServerboundTeleportHomePacket::handle
        );

        registrar.playToServer(
                ServerboundSetHomePacket.TYPE,
                ServerboundSetHomePacket.STREAM_CODEC,
                ServerboundSetHomePacket::handle
        );
    }
}




