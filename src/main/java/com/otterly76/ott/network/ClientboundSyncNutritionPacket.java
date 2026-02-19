package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ClientboundSyncNutritionPacket(float saturation, float exhaustion) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundSyncNutritionPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sync_nutrition"));

    public static final StreamCodec<ByteBuf, ClientboundSyncNutritionPacket> STREAM_CODEC = StreamCodec.composite(
            net.minecraft.network.codec.ByteBufCodecs.FLOAT, ClientboundSyncNutritionPacket::saturation,
            net.minecraft.network.codec.ByteBufCodecs.FLOAT, ClientboundSyncNutritionPacket::exhaustion,
            ClientboundSyncNutritionPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
