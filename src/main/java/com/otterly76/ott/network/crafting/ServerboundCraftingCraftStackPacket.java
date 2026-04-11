package com.otterly76.ott.network.crafting;

import com.otterly76.ott.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundCraftingCraftStackPacket(int slotIndex) implements CustomPacketPayload {

    public static final Type<ServerboundCraftingCraftStackPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crafting_craft_stack"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundCraftingCraftStackPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ServerboundCraftingCraftStackPacket::slotIndex,
                    ServerboundCraftingCraftStackPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundCraftingCraftStackPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            AbstractContainerMenu menu = player.containerMenu;
            if (payload.slotIndex() < 0 || payload.slotIndex() >= menu.slots.size())
                return;

            Slot mouseSlot = menu.slots.get(payload.slotIndex());
            ItemStack mouseStack = menu.getCarried();
            int maxTries = 64;
            while (maxTries-- > 0 && mouseSlot.hasItem()
                    && (mouseStack.isEmpty() || mouseStack.getCount() + mouseSlot.getItem().getCount() <= mouseStack.getMaxStackSize())) {
                menu.clicked(mouseSlot.index, 0, ClickType.PICKUP, player);
                mouseStack = menu.getCarried();
            }

            player.connection.send(new ClientboundContainerSetSlotPacket(-1, -1, -1, menu.getCarried()));
        });
    }
}
