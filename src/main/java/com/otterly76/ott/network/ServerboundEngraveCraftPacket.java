package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.EngravingTableMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundEngraveCraftPacket(ItemStack stack, boolean replaceAll) implements CustomPacketPayload {

    public static final Type<ServerboundEngraveCraftPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "engrave_craft"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundEngraveCraftPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC,
                    ServerboundEngraveCraftPacket::stack,
                    ByteBufCodecs.BOOL,
                    ServerboundEngraveCraftPacket::replaceAll,
                    ServerboundEngraveCraftPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundEngraveCraftPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player
                    && player.containerMenu instanceof EngravingTableMenu menu) {
                menu.craft(packet.stack(), packet.replaceAll());
                player.containerMenu.broadcastChanges();
            }
        });
    }
}
