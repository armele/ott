package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.storage.HomeSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record ServerboundSetHomePacket(String name) implements CustomPacketPayload {
    public static final Type<ServerboundSetHomePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "set_home"));

    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, ServerboundSetHomePacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
            net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8,
            ServerboundSetHomePacket::name,
            ServerboundSetHomePacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundSetHomePacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!OttConfig.HOMES.ENABLED.get()) {
                context.player().sendSystemMessage(Component.literal("Home system is disabled."));
                return;
            }

            ServerPlayer player = (ServerPlayer) context.player();
            HomeSavedData data = HomeSavedData.get(player.serverLevel());
            Map<String, HomeSavedData.HomePos> homes = data.getHomes(player.getUUID());

            int maxHomes = OttConfig.HOMES.MAX_HOMES.get();
            if (maxHomes != -1 && homes.size() >= maxHomes && !homes.containsKey(payload.name().toLowerCase())) {
                player.sendSystemMessage(Component.literal("You have reached the maximum number of homes (" + maxHomes + ")."));
                return;
            }

            data.setHome(player.getUUID(), payload.name(), player.blockPosition(), player.level().dimension());
            player.sendSystemMessage(Component.literal("Home '" + payload.name() + "' set."));
        });
    }
}