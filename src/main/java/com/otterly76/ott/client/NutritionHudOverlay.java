package com.otterly76.ott.client;

import com.otterly76.ott.Constants;
import com.otterly76.ott.util.item.FoodUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NutritionHudOverlay implements LayeredDraw.Layer {

    private static final ResourceLocation HUNGER_PREVIEW_FULL = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/hunger_preview_full");
    private static final ResourceLocation HUNGER_PREVIEW_HALF = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/hunger_preview_half");
    private static final ResourceLocation SATURATION_OVERLAY = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/saturation_overlay");
    private static final ResourceLocation SATURATION_OVERLAY_HALF = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/saturation_overlay_half");

    private static final ResourceLocation FOOD_EMPTY = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/food_empty");
    private static final ResourceLocation FOOD_FULL = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/food_full");
    private static final ResourceLocation FOOD_HALF = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/food_half");

    private static final ResourceLocation EXHAUSTION_OVERLAY = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/exhaustion_overlay");

    private static final ResourceLocation FOOD_EMPTY_ROTTEN = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/food_empty_rotten");
    private static final ResourceLocation FOOD_FULL_ROTTEN = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/food_full_rotten");
    private static final ResourceLocation FOOD_HALF_ROTTEN = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hud/food_half_rotten");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;

        Player player = mc.player;
        if (player == null || player.isSpectator() || mc.gameMode == null || !mc.gameMode.canHurtPlayer()) return;

        FoodData stats = player.getFoodData();
        int left = mc.getWindow().getGuiScaledWidth() / 2 + 91;
        int top = mc.getWindow().getGuiScaledHeight() - 39;

        renderCurrentHunger(guiGraphics, player, stats, left, top);

        renderSaturationOverlay(guiGraphics, stats, left, top);

        ItemStack heldItem = player.getMainHandItem();
        if (FoodUtil.getFoodValues(heldItem, player) == null) {
            heldItem = player.getOffhandItem();
        }

        renderFoodPreview(guiGraphics, player, heldItem, stats, left, top);
    }

    private static void renderCurrentHunger(GuiGraphics guiGraphics, Player player, FoodData stats, int left, int top) {
        int level = stats.getFoodLevel();
        boolean hasHungerEffect = player.hasEffect(MobEffects.HUNGER);

        ResourceLocation empty = hasHungerEffect ? FOOD_EMPTY_ROTTEN : FOOD_EMPTY;
        ResourceLocation full = hasHungerEffect ? FOOD_FULL_ROTTEN : FOOD_FULL;
        ResourceLocation half = hasHungerEffect ? FOOD_HALF_ROTTEN : FOOD_HALF;

        for (int i = 0; i < 10; ++i) {
            int x = left - i * 8 - 9;
            guiGraphics.blitSprite(empty, x, top, 9, 9);
        }

        renderExhaustion(guiGraphics, stats, left, top);

        for (int i = 0; i < 10; ++i) {
            int x = left - i * 8 - 9;
            if (level > i * 2 + 1) {
                guiGraphics.blitSprite(full, x, top, 9, 9);
            } else if (level > i * 2) {
                guiGraphics.blitSprite(half, x, top, 9, 9);
            }
        }
    }

    private static void renderExhaustion(GuiGraphics guiGraphics, FoodData stats, int left, int top) {
        float exhaustion = stats.getExhaustionLevel();
        float ratio = Math.min(exhaustion / 4.0f, 1.0f);

        if (ratio <= 0) return;

        int barWidth = 81;
        int drawWidth = (int) (ratio * barWidth);

        for (int i = 0; i < 10; ++i) {
            int visibleWidth = Math.max(0, Math.min(9, drawWidth - i * 8));

            if (visibleWidth > 0) {
                int x = left - i * 8 - 9;
                guiGraphics.blitSprite(EXHAUSTION_OVERLAY, 9, 9, 9 - visibleWidth, 0, x + 9 - visibleWidth, top, visibleWidth, 9);
            }
        }
    }

    private static void renderSaturationOverlay(GuiGraphics guiGraphics, FoodData stats, int left, int top) {
        float saturation = stats.getSaturationLevel();

        for (int i = 0; i < 10; ++i) {
            int x = left - i * 8 - 9;

            if (saturation >= (i + 1) * 2) {
                guiGraphics.blitSprite(SATURATION_OVERLAY, x, top, 9, 9);
            }

            else if (saturation > i * 2) {
                guiGraphics.blitSprite(SATURATION_OVERLAY_HALF, x, top, 9, 9);
            }
        }
    }

    private static void renderFoodPreview(GuiGraphics guiGraphics, Player player, ItemStack stack, FoodData stats, int left, int top) {
        FoodUtil.FoodValues values = FoodUtil.getFoodValues(stack, player);
        if (values == null) return;

        int currentLevel = stats.getFoodLevel();
        int newLevel = currentLevel + values.hunger();

        for (int i = currentLevel; i < newLevel && i < 20; ++i) {
            if (i % 2 != 0 && i != currentLevel) continue;

            int x = left - (i / 2) * 8 - 9;

            ResourceLocation sprite;
            if (i % 2 != 0) {
                sprite = HUNGER_PREVIEW_FULL;
            } else {
                sprite = (i + 1 < newLevel) ? HUNGER_PREVIEW_FULL : HUNGER_PREVIEW_HALF;
            }

            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 0.5f);
            guiGraphics.blitSprite(sprite, x, top, 9, 9);
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
