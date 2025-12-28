package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.TrashMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundOpenTrashPacket() implements CustomPacketPayload {
    public static final Type<ServerboundOpenTrashPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "open_trash"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundOpenTrashPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new TrashMenu(id, inv),
                    Component.literal("TRASH")
            ));
        });
    }
}