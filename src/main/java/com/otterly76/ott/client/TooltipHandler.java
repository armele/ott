package com.otterly76.ott.client;

import com.mojang.datafixers.util.Either;
import com.otterly76.ott.Constants;
import com.otterly76.ott.client.tooltip.FoodTooltipComponent;
import com.otterly76.ott.util.item.FoodUtil;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class TooltipHandler {

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        FoodUtil.FoodValues values = FoodUtil.getFoodValues(event.getItemStack(), player);
        if (values != null) {
            event.getTooltipElements().add(Either.right(new FoodTooltipComponent(values.hunger(), values.saturation())));
        }
    }
}
