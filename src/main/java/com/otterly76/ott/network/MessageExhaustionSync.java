package com.otterly76.ott.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record MessageExhaustionSync(float exhaustionLevel) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageExhaustionSync> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hunger", "exhaustion"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageExhaustionSync> CODEC;

    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageExhaustionSync message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> ctx.player().getFoodData().setExhaustion(message.exhaustionLevel()));
    }

    static {
        CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, MessageExhaustionSync::exhaustionLevel, MessageExhaustionSync::new);
    }
}