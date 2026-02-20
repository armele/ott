package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record S2CSyncAFKStatusPacket(UUID playerUUID, boolean afk) implements CustomPacketPayload {
    public static final Type<S2CSyncAFKStatusPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_afk_status"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncAFKStatusPacket> STREAM_CODEC = StreamCodec.composite(
            net.minecraft.core.UUIDUtil.STREAM_CODEC, S2CSyncAFKStatusPacket::playerUUID,
            ByteBufCodecs.BOOL, S2CSyncAFKStatusPacket::afk,
            S2CSyncAFKStatusPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}