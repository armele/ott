package com.otterly76.ott.network.recycling;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.RecyclingMenu;
import com.otterly76.ott.recycling.RecyclingManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundOpenRecyclingPacket() implements CustomPacketPayload {
    public static final Type<ServerboundOpenRecyclingPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "open_recycling"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundOpenRecyclingPacket> STREAM_CODEC =
            StreamCodec.unit(new ServerboundOpenRecyclingPacket());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundOpenRecyclingPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            RecyclingManager.createSession(player);
            player.openMenu(new SimpleMenuProvider(
                    RecyclingMenu::createForServer,
                    Component.literal("Recycling")
            ));
        });
    }
}
