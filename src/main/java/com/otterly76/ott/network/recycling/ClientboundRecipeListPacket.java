package com.otterly76.ott.network.recycling;

import com.otterly76.ott.Constants;
import com.otterly76.ott.recycling.RecyclingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ClientboundRecipeListPacket(List<RecyclingRecipe> recipes, int size, boolean shouldSendPacket)
        implements CustomPacketPayload {

    public static final Type<ClientboundRecipeListPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycling_recipe_list"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRecipeListPacket> STREAM_CODEC =
            StreamCodec.composite(
                    RecyclingRecipe.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    ClientboundRecipeListPacket::recipes,
                    ByteBufCodecs.INT,
                    ClientboundRecipeListPacket::size,
                    ByteBufCodecs.BOOL,
                    ClientboundRecipeListPacket::shouldSendPacket,
                    ClientboundRecipeListPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
