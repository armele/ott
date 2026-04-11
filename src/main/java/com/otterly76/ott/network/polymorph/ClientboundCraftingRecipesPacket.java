package com.otterly76.ott.network.polymorph;

import com.otterly76.ott.Constants;
import com.otterly76.ott.polymorph.CraftingRecipePair;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ClientboundCraftingRecipesPacket(List<CraftingRecipePair> recipes,
                                               Optional<ResourceLocation> selected)
        implements CustomPacketPayload {

    public static final Type<ClientboundCraftingRecipesPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crafting_recipes"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCraftingRecipesPacket> STREAM_CODEC =
            StreamCodec.composite(
                    CraftingRecipePair.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
                    ClientboundCraftingRecipesPacket::recipes,
                    ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                    ClientboundCraftingRecipesPacket::selected,
                    ClientboundCraftingRecipesPacket::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
