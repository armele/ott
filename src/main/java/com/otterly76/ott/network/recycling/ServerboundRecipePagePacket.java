package com.otterly76.ott.network.recycling;

import com.otterly76.ott.Constants;
import com.otterly76.ott.recycling.RecyclingManager;
import com.otterly76.ott.recycling.RecyclingSession;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundRecipePagePacket(int page) implements CustomPacketPayload {
    public static final Type<ServerboundRecipePagePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycling_page_change"));

    public static final StreamCodec<ByteBuf, ServerboundRecipePagePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    ServerboundRecipePagePacket::page,
                    ServerboundRecipePagePacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundRecipePagePacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            RecyclingSession session = RecyclingManager.getSession(player.getUUID());
            if (session != null) {
                session.updatePage(payload.page());
            }
        });
    }
}
