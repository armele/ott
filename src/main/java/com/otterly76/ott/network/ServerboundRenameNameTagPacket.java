package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundRenameNameTagPacket() implements CustomPacketPayload {
    public static final Type<ServerboundRenameNameTagPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "rename_name_tag"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundRenameNameTagPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!OttConfig.VISUALS.NAME_TAG_DIRECT_RENAME.get()) return;
            
            ItemStack stack = context.player().getMainHandItem();
            if (!stack.is(Items.NAME_TAG)) return;

            context.player().openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new AnvilMenu(id, inv, ContainerLevelAccess.NULL),
                    Component.translatable("container.repair")
            ));
        });
    }
}