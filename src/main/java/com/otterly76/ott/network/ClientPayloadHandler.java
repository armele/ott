package com.otterly76.ott.network;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleSyncNutrition(final ClientboundSyncNutritionPacket packet, final IPayloadContext context) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.getFoodData().setSaturation(packet.saturation());
            player.getFoodData().setExhaustion(packet.exhaustion());
        }
    }

    public static void handleOpenNameTagEditor(final S2COpenNameTagEditorMessage packet, final IPayloadContext context) {
        Minecraft.getInstance().setScreen(new com.otterly76.ott.client.gui.NameTagEditScreen(packet.hand(), packet.title()));
    }

    public static void handleAnvilRepair(final S2CAnvilRepairMessage packet, final IPayloadContext context) {
        var level = Minecraft.getInstance().level;
        if (level != null) {
            level.levelEvent(1030, packet.pos(), 0);
            Minecraft.getInstance().particleEngine.destroy(packet.pos(), net.minecraft.world.level.block.Block.stateById(packet.stateId()));
        }
    }

    public static void handleSyncAFKStatus(final S2CSyncAFKStatusPacket packet, final IPayloadContext context) {
        com.otterly76.ott.afk.AFKClientStates.setAFK(packet.playerUUID(), packet.afk());
    }
}
