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
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundCraftingTransferPacket(int slotIndex) implements CustomPacketPayload {

    public static final Type<ServerboundCraftingTransferPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crafting_transfer"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundCraftingTransferPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ServerboundCraftingTransferPacket::slotIndex,
                    ServerboundCraftingTransferPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundCraftingTransferPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            AbstractContainerMenu menu = player.containerMenu;
            if (payload.slotIndex() < 0 || payload.slotIndex() >= menu.slots.size())
                return;
            CraftingTweaksGrid grid = CraftingTweaksHelper.getGrid(menu, player);
            if (grid == null)
                return;
            Slot slot = menu.slots.get(payload.slotIndex());
            CraftingTweaksHelper.transferIntoGrid(grid, menu, player, slot);
        });
    }
}
