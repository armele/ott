package com.otterly76.ott.network;

import com.otterly76.ott.Ott;
import com.otterly76.ott.util.item.BundleFeatures;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundSelectBundleItemPacket(int slotId, int selectedItemIndex) implements CustomPacketPayload {
    public static final Type<ServerboundSelectBundleItemPacket> TYPE = new Type<>(Ott.resource("serverbound_select_bundle_item"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSelectBundleItemPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundSelectBundleItemPacket::write, ServerboundSelectBundleItemPacket::new);

    public ServerboundSelectBundleItemPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
        if (this.selectedItemIndex < 0 && this.selectedItemIndex != -1) {
            throw new IllegalArgumentException("Invalid selectedItemIndex: " + this.selectedItemIndex);
        }
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.slotId);
        buf.writeVarInt(this.selectedItemIndex);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundSelectBundleItemPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            NonNullList<Slot> slots = context.player().containerMenu.slots;
            int slot = packet.slotId;
            if (slot >= 0 && slot < slots.size()) {
                ItemStack stack = slots.get(slot).getItem();
                BundleFeatures.toggleSelectedItem(stack, packet.selectedItemIndex);
            }
        });
    }
}
