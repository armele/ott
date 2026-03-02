package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.afk.AFKServerEvents;
import com.otterly76.ott.registry.ModAttachmentTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record C2SNotifyActionPacket() implements CustomPacketPayload {
    public static final Type<C2SNotifyActionPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "notify_action"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SNotifyActionPacket> STREAM_CODEC = StreamCodec.unit(new C2SNotifyActionPacket());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final C2SNotifyActionPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                var afkState = player.getData(ModAttachmentTypes.AFK_STATE);
                AFKServerEvents.resetAFK(player, afkState);
            }
        });
    }
}
