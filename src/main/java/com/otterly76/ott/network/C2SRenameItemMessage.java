package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record C2SRenameItemMessage(String name) implements CustomPacketPayload {
    public static final Type<C2SRenameItemMessage> TYPE = new Type<>(Constants.loc("rename_item"));

    public static final StreamCodec<FriendlyByteBuf, C2SRenameItemMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, C2SRenameItemMessage::name,
            C2SRenameItemMessage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(final IPayloadContext context) {
        context.enqueueWork(() -> {
            AbstractContainerMenu menu = context.player().containerMenu;
            if (menu instanceof AnvilMenu anvilMenu) {
                if (!anvilMenu.stillValid(context.player())) {
                    return;
                }

                anvilMenu.setItemName(this.name);
            }
        });
    }
}
