package com.otterly76.ott.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record MessageSaturationSync(float saturationLevel) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageSaturationSync> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hunger", "saturation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSaturationSync> CODEC;

    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageSaturationSync message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> ctx.player().getFoodData().setSaturation(message.saturationLevel()));
    }

    static {
        CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, MessageSaturationSync::saturationLevel, MessageSaturationSync::new);
    }
}