package com.otterly76.ott.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.otterly76.ott.Constants.MOD_ID;

@SuppressWarnings("DuplicatedCode")
public class ClientFoodTooltipComponent implements ClientTooltipComponent {
    private static final ResourceLocation FOOD_FULL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/food_full");
    private static final ResourceLocation FOOD_HALF = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/food_half");
    private static final ResourceLocation SATURATION = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/saturation_overlay");
    private static final ResourceLocation SATURATION_HALF = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/saturation_overlay_half");

    // Status Heart Sprites
    private static final ResourceLocation WHITE_FULL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/white_full");
    private static final ResourceLocation WHITE_HALF = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/white_half");
    private static final ResourceLocation FROZEN_FULL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/frozen_full");
    private static final ResourceLocation FROZEN_HALF = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/frozen_half");
    private static final ResourceLocation POISONED_FULL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/poisoned_full");
    private static final ResourceLocation POISONED_HALF = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/poisoned_half");
    private static final ResourceLocation WITHERED_FULL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/withered_full");
    private static final ResourceLocation WITHERED_HALF = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/withered_half");

    private final FoodTooltipComponent data;

    public ClientFoodTooltipComponent(FoodTooltipComponent data) {
        this.data = data;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        int hungerIcons = (int) Math.ceil(data.hunger() / 2.0);
        int saturationIcons = (int) Math.ceil(data.saturation() / 2.0);
        return Math.max(hungerIcons, saturationIcons) * 9;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int hunger = data.hunger();
        float saturation = data.saturation();
        int hungerIcons = (int) Math.ceil(hunger / 2.0);

        // Determine Status
        boolean isFrozen = player.isFullyFrozen();
        boolean isPoisoned = player.hasEffect(MobEffects.POISON);
        boolean isWithered = player.hasEffect(MobEffects.WITHER);

        // --- LINE 1: Hunger (with status checks) ---
        for (int i = 0; i < hungerIcons; i++) {
            int xPos = x + (i * 9);
            boolean isHalf = (i * 2 + 1 >= hunger);

            if (isFrozen || isPoisoned || isWithered) {
                // Draw Tinted Background first
                int bgColor = isFrozen ? 0xADF3FF : (isPoisoned ? 0x94D11F : 0x422020);
                renderTintedSprite(guiGraphics, isHalf ? WHITE_HALF : WHITE_FULL, xPos, y, bgColor);

                // Draw status overlay on top
                ResourceLocation statusSprite = getStatusSprite(isFrozen, isPoisoned, isWithered, isHalf);
                guiGraphics.blitSprite(statusSprite, xPos, y, 9, 9);
            } else {
                // Normal Apples
                guiGraphics.blitSprite(isHalf ? FOOD_HALF : FOOD_FULL, xPos, y, 9, 9);
            }
        }

        // --- LINE 2: Saturation ---
        int saturationY = y + 10;
        int saturationIcons = (int) Math.ceil(saturation / 2.0);

        RenderSystem.enableBlend();
        for (int i = 0; i < saturationIcons; i++) {
            int xPos = x + (i * 9);
            if (saturation >= (i + 1) * 2) {
                guiGraphics.blitSprite(SATURATION, xPos, saturationY, 9, 9);
            } else if (saturation > i * 2) {
                guiGraphics.blitSprite(SATURATION_HALF, xPos, saturationY, 9, 9);
            }
        }
        RenderSystem.disableBlend();
    }

    private ResourceLocation getStatusSprite(boolean frozen, boolean poisoned, boolean withered, boolean half) {
        if (frozen) return half ? FROZEN_HALF : FROZEN_FULL;
        if (poisoned) return half ? POISONED_HALF : POISONED_FULL;
        return half ? WITHERED_HALF : WITHERED_FULL;
    }

    private void renderTintedSprite(GuiGraphics guiGraphics, ResourceLocation sprite, int x, int y, int color) {
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        guiGraphics.setColor(r, g, b, 1.0F);
        guiGraphics.blitSprite(sprite, x, y, 9, 9);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
