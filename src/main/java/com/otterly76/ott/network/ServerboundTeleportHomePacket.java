package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.storage.HomeSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundTeleportHomePacket(String name) implements CustomPacketPayload {
    public static final Type<ServerboundTeleportHomePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "teleport_home"));

    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, ServerboundTeleportHomePacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
            net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8,
            ServerboundTeleportHomePacket::name,
            ServerboundTeleportHomePacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundTeleportHomePacket payload, final IPayloadContext context) {
        if (!OttConfig.HOMES.ENABLED.get()) return;
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            HomeSavedData data = HomeSavedData.get(player.serverLevel());
            HomeSavedData.HomePos home = data.getHome(player.getUUID(), payload.name());

            if (home == null) {
                player.sendSystemMessage(Component.literal("Home '" + payload.name() + "' not found."));
                return;
            }

            ServerLevel level = player.server.getLevel(home.dimension());
            if (level == null) {
                player.sendSystemMessage(Component.literal("Dimension for home '" + payload.name() + "' no longer exists."));
                return;
            }

            player.teleportTo(level, home.pos().getX() + 0.5, home.pos().getY(), home.pos().getZ() + 0.5, player.getYRot(), player.getXRot());
            player.sendSystemMessage(Component.literal("Teleported to '" + payload.name() + "'."));
        });
    }
}