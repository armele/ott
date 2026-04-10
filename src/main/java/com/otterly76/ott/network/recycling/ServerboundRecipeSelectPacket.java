package com.otterly76.ott.network.recycling;

import com.otterly76.ott.Constants;
import com.otterly76.ott.recycling.RecyclingManager;
import com.otterly76.ott.recycling.RecyclingRecipe;
import com.otterly76.ott.recycling.RecyclingSession;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundRecipeSelectPacket(RecyclingRecipe recipe) implements CustomPacketPayload {
    public static final Type<ServerboundRecipeSelectPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycling_recipe_select"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRecipeSelectPacket> STREAM_CODEC =
            StreamCodec.composite(
                    RecyclingRecipe.STREAM_CODEC,
                    ServerboundRecipeSelectPacket::recipe,
                    ServerboundRecipeSelectPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundRecipeSelectPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            RecyclingSession session = RecyclingManager.getSession(player.getUUID());
            if (session != null) {
                session.handleRecipeSelection(payload.recipe());
            }
        });
    }
}
