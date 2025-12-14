package com.otterly76.ott.client;

import com.otterly76.ott.Constants;
import com.otterly76.ott.util.FoodUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import java.util.Locale;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class TooltipHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        FoodUtil.FoodValues values = FoodUtil.getFoodValues(event.getItemStack(), event.getEntity());
        if (values != null) {
            event.getToolTip().add(Component.literal(" "));
            event.getToolTip().add(Component.translatable("tooltip.ott.hunger", values.hunger()).withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("tooltip.ott.saturation", String.format(Locale.ROOT, "%.1f", values.saturation())).withStyle(ChatFormatting.GOLD));
        }
    }
}