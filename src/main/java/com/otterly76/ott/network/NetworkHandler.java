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
                (packet, context) -> context.enqueueWork(() -> ClientPayloadHandler.handleSyncNutrition(packet, context))
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

        registrar.playToServer(
                ServerboundOpenItemPacket.TYPE,
                ServerboundOpenItemPacket.STREAM_CODEC,
                ServerboundOpenItemPacket::handle
        );

        registrar.playToClient(
                S2COpenNameTagEditorMessage.TYPE,
                S2COpenNameTagEditorMessage.STREAM_CODEC,
                (packet, context) -> context.enqueueWork(() -> ClientPayloadHandler.handleOpenNameTagEditor(packet, context))
        );

        registrar.playToServer(
                C2SNameTagUpdateMessage.TYPE,
                C2SNameTagUpdateMessage.STREAM_CODEC,
                C2SNameTagUpdateMessage::handle
        );

        registrar.playToClient(
                S2CAnvilRepairMessage.TYPE,
                S2CAnvilRepairMessage.STREAM_CODEC,
                (packet, context) -> context.enqueueWork(() -> ClientPayloadHandler.handleAnvilRepair(packet, context))
        );

        registrar.playToServer(
                C2SRenameItemMessage.TYPE,
                C2SRenameItemMessage.STREAM_CODEC,
                C2SRenameItemMessage::handle
        );

        registrar.playToServer(
                ServerboundSelectBundleItemPacket.TYPE,
                ServerboundSelectBundleItemPacket.STREAM_CODEC,
                ServerboundSelectBundleItemPacket::handle
        );

        registrar.playToClient(
                S2CSyncAFKStatusPacket.TYPE,
                S2CSyncAFKStatusPacket.STREAM_CODEC,
                (packet, context) -> context.enqueueWork(() -> ClientPayloadHandler.handleSyncAFKStatus(packet, context))
        );

        registrar.playToServer(
                C2SNotifyActionPacket.TYPE,
                C2SNotifyActionPacket.STREAM_CODEC,
                C2SNotifyActionPacket::handle
        );
    }
}