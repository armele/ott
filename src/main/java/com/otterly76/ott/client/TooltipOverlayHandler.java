package com.otterly76.ott.client;

import com.mojang.datafixers.util.Either;
import com.otterly76.ott.api.event.TooltipOverlayEvent;
import com.otterly76.ott.helpers.FoodHelper;
import com.otterly76.ott.helpers.KeyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.NeoForge;

public class TooltipOverlayHandler {
    public static void init() {
        NeoForge.EVENT_BUS.register(new TooltipOverlayHandler());
    }

    public static void register(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(FoodTooltip.class, FoodTooltipRenderer::new);
    }

    @SubscribeEvent
    public void gatherTooltips(RenderTooltipEvent.GatherComponents event) {
        if (!event.isCanceled()) {
            ItemStack hoveredStack = event.getItemStack();
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            if (shouldShowTooltip(hoveredStack, mc.player)) {
                FoodHelper.QueriedFoodResult queriedFoodResult = FoodHelper.query(hoveredStack, mc.player);
                if (queriedFoodResult != null) {
                    FoodProperties defaultFood = queriedFoodResult.defaultFoodProperties;
                    FoodProperties modifiedFood = queriedFoodResult.modifiedFoodProperties;
                    TooltipOverlayEvent.Pre prerenderEvent = new TooltipOverlayEvent.Pre(hoveredStack, defaultFood, modifiedFood);
                    NeoForge.EVENT_BUS.post(prerenderEvent);
                    if (!prerenderEvent.isCanceled()) {
                        FoodTooltip foodTooltip = new FoodTooltip(prerenderEvent.itemStack, prerenderEvent.defaultFood, prerenderEvent.modifiedFood, mc.player);
                        if (foodTooltip.shouldRenderHungerBars()) {
                            event.getTooltipElements().add(Either.right(foodTooltip));
                        }
                    }
                }
            }
        }
    }

    private static boolean shouldShowTooltip(ItemStack hoveredStack, Player player) {
        if (hoveredStack.isEmpty()) {
            return false;
        } else if (hoveredStack.has(DataComponents.HIDE_TOOLTIP)) {
            return false;
        } else {
            boolean shouldShowTooltip = KeyHelper.isShiftKeyDown();
            if (!shouldShowTooltip) {
                return false;
            } else {
                return FoodHelper.isFood(hoveredStack, player);
            }
        }
    }
}