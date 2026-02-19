package com.otterly76.ott.client.handler;


import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.util.gui.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NameTagTooltipHandler {
    public static final String KEY_NAME_TAG_DESCRIPTION = "ott.item.name_tag.description";

    public static void onItemTooltip(ItemStack itemStack, List<Component> lines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        if (OttConfig.ANVILS.NAME_TAG_TOOLTIP.get()) {
            if (OttConfig.ANVILS.MISC.EDIT_NAME_TAGS_NO_ANVIL.get()) {
                if (itemStack.is(Items.NAME_TAG)) {
                    Component sneakComponent = Component.keybind("key.sneak").withStyle(ChatFormatting.LIGHT_PURPLE);
                    Component useComponent = Component.keybind("key.use").withStyle(ChatFormatting.LIGHT_PURPLE);
                    Component component = Component.translatable("ott.item.name_tag.description", sneakComponent, useComponent).withStyle(ChatFormatting.GRAY);
                    List<Component> components = TooltipUtil.splitTooltipLines(component);
                    if (tooltipFlag.isAdvanced()) {
                        lines.addAll(lines.size() - (!itemStack.getComponents().isEmpty() ? 2 : 1), components);
                    } else {
                        lines.addAll(components);
                    }
                }

            }
        }
    }
}
