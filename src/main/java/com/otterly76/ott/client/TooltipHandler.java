package com.otterly76.ott.client;

import com.mojang.datafixers.util.Either;
import com.otterly76.ott.Constants;
import com.otterly76.ott.client.tooltip.FoodTooltipComponent;
import com.otterly76.ott.util.ModTags;
import com.otterly76.ott.util.item.FoodUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class TooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof BlockItem bi) {
            if (bi.getBlock().defaultBlockState().is(ModTags.Blocks.CTM_BLOCKS)) {
                event.getToolTip().add(Component.translatable("tooltip.ott.connecting")
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }

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
