package com.otterly76.ott.network.recycling;

import com.otterly76.ott.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ClientboundRecipeSelectRequestPacket() implements CustomPacketPayload {
    public static final Type<ClientboundRecipeSelectRequestPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycling_recipe_select_request"));

    public static final StreamCodec<FriendlyByteBuf, ClientboundRecipeSelectRequestPacket> STREAM_CODEC =
            StreamCodec.of((buf, val) -> {}, buf -> new ClientboundRecipeSelectRequestPacket());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
