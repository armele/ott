package com.otterly76.ott.network.polymorph;

import com.otterly76.ott.Constants;
import com.otterly76.ott.polymorph.CraftingRecipeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundSelectCraftingRecipePacket(ResourceLocation recipeId)
        implements CustomPacketPayload {

    public static final Type<ServerboundSelectCraftingRecipePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "select_crafting_recipe"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundSelectCraftingRecipePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    ServerboundSelectCraftingRecipePacket::recipeId,
                    ServerboundSelectCraftingRecipePacket::new);

    public static void handle(ServerboundSelectCraftingRecipePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            player.level().getRecipeManager().byKey(packet.recipeId()).ifPresent(recipe -> {
                CraftingRecipeManager.setSelected(player.getUUID(), recipe);
                player.containerMenu.slotsChanged(player.getInventory());
            });
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
