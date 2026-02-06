package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.TrashMenu;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundConfirmTrashPacket() implements CustomPacketPayload {
    public static final Type<ServerboundConfirmTrashPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "confirm_trash"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundConfirmTrashPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof TrashMenu trashMenu) {
                trashMenu.setDeleteOnClose(true);
                // We don't call closeContainer() here anymore to let the client handle the UI transition smoothly
            }
        });
    }
}
