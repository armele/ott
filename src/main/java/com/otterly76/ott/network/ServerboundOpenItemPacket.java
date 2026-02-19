package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.util.item.InventoryUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundOpenItemPacket(int slotIndex) implements CustomPacketPayload {
    public static final Type<ServerboundOpenItemPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "open_item"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundOpenItemPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerboundOpenItemPacket::slotIndex,
            ServerboundOpenItemPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundOpenItemPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                InventoryUtils.openItemGui(player, payload.slotIndex());
            }
        });
    }
}
