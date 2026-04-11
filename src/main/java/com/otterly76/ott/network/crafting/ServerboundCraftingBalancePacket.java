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

public record ServerboundCraftingBalancePacket(boolean spread) implements CustomPacketPayload {

    public static final Type<ServerboundCraftingBalancePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crafting_balance"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundCraftingBalancePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    ServerboundCraftingBalancePacket::spread,
                    ServerboundCraftingBalancePacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundCraftingBalancePacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            AbstractContainerMenu menu = player.containerMenu;
            CraftingTweaksGrid grid = CraftingTweaksHelper.getGrid(menu, player);
            if (grid == null)
                return;
            if (payload.spread()) {
                CraftingTweaksHelper.spreadGrid(grid, menu);
            } else {
                CraftingTweaksHelper.balanceGrid(grid, menu);
            }
        });
    }
}
