package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.NotNull;

public record S2COpenNameTagEditorMessage(InteractionHand hand, Component title) implements CustomPacketPayload {
    public static final Type<S2COpenNameTagEditorMessage> TYPE = new Type<>(Constants.loc("open_name_tag_editor"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenNameTagEditorMessage> STREAM_CODEC = StreamCodec.composite(
            net.minecraft.network.codec.ByteBufCodecs.VAR_INT.map(i -> InteractionHand.values()[i], Enum::ordinal), S2COpenNameTagEditorMessage::hand,
            net.minecraft.network.chat.ComponentSerialization.STREAM_CODEC, S2COpenNameTagEditorMessage::title,
            S2COpenNameTagEditorMessage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
