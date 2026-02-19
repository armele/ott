package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import com.otterly76.ott.util.data.ComponentDecomposer;
import com.otterly76.ott.util.data.FormattedStringDecomposer;
import com.otterly76.ott.inventory.ModAnvilMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record C2SNameTagUpdateMessage(InteractionHand hand, String title) implements CustomPacketPayload {
    public static final Type<C2SNameTagUpdateMessage> TYPE = new Type<>(Constants.loc("name_tag_update"));

    public static final StreamCodec<FriendlyByteBuf, C2SNameTagUpdateMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT.map(i -> InteractionHand.values()[i], Enum::ordinal), C2SNameTagUpdateMessage::hand,
            ByteBufCodecs.STRING_UTF8, C2SNameTagUpdateMessage::title,
            C2SNameTagUpdateMessage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(final IPayloadContext context) {
        context.enqueueWork(() -> {
            ItemStack stack = context.player().getItemInHand(this.hand);
            if (stack.is(Items.NAME_TAG)) {
                String s = FormattedStringDecomposer.filterText(this.title);
                if (ComponentDecomposer.getStringLength(s) <= 50) {
                    ModAnvilMenu.setFormattedItemName(s.trim(), stack);
                }
            }
        });
    }
}
