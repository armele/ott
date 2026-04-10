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

public record ServerboundRecycleButtonClickPacket(boolean hasShiftDown) implements CustomPacketPayload {
    public static final Type<ServerboundRecycleButtonClickPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycle_button_click"));

    public static final StreamCodec<ByteBuf, ServerboundRecycleButtonClickPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    ServerboundRecycleButtonClickPacket::hasShiftDown,
                    ServerboundRecycleButtonClickPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundRecycleButtonClickPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            RecyclingSession session = RecyclingManager.getSession(player.getUUID());
            if (session != null) {
                session.handleUncraftButtonClicked(payload.hasShiftDown());
            }
        });
    }
}
