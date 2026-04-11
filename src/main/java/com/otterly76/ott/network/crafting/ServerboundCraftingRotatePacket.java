package com.otterly76.ott.network.crafting;

import com.otterly76.ott.Constants;
import com.otterly76.ott.crafting.CraftingTweaksGrid;
import com.otterly76.ott.crafting.CraftingTweaksHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundCraftingRotatePacket(boolean reverse) implements CustomPacketPayload {

    public static final Type<ServerboundCraftingRotatePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crafting_rotate"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundCraftingRotatePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    ServerboundCraftingRotatePacket::reverse,
                    ServerboundCraftingRotatePacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundCraftingRotatePacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            AbstractContainerMenu menu = player.containerMenu;
            CraftingTweaksGrid grid = CraftingTweaksHelper.getGrid(menu, player);
            if (grid == null)
                return;
            CraftingTweaksHelper.rotateGrid(grid, menu, payload.reverse());
        });
    }
}
