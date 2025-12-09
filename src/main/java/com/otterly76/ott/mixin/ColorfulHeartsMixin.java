package com.otterly76.ott.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class ColorfulHeartsMixin {

    // Define colors for decks 1 through 7 (Deck 0 is vanilla red)
    @Unique
    private static final int[] DECK_COLORS = new int[]{
            0xb02e26, // Deck 0: Red
            0xf9801d, // Deck 1: Orange
            0xfed83d, // Deck 2: Yellow
            0x5e7c16, // Deck 3: Green
            0x3c44aa, // Deck 4: Blue
            0x8932b8, // Deck 5: Purple
            0xFF9999, // Deck 6: Pink
            0xFFD700  // Deck 7: Gold
    };

    @Unique
    private static final ResourceLocation WHITE_HEART_FULL = ResourceLocation.fromNamespaceAndPath("ott", "hud/heart/white_full");
    @Unique
    private static final ResourceLocation WHITE_HEART_HALF = ResourceLocation.fromNamespaceAndPath("ott", "hud/heart/white_half");

    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    private void ott$renderColorfulHearts(GuiGraphics guiGraphics, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean renderHighlight, CallbackInfo ci) {
        ci.cancel();

        Gui.HeartType heartType = Gui.HeartType.NORMAL;
        boolean isHardcore = player.level().getLevelData().isHardcore();

        if (player.isFullyFrozen()) {
            heartType = Gui.HeartType.FROZEN;
        } else if (player.hasEffect(MobEffects.WITHER)) {
            heartType = Gui.HeartType.WITHERED;
        } else if (player.hasEffect(MobEffects.POISON)) {
            heartType = Gui.HeartType.POISIONED;
        }

        int totalHealth = Mth.ceil(player.getHealth());
        int totalAbsorb = Mth.ceil(player.getAbsorptionAmount());
        int healthDeck = Math.max(0, (totalHealth - 1) / 20);
        int absorbDeck = Math.max(0, (totalAbsorb - 1) / 20);

        int heartCount = Mth.ceil((double) maxHealth / 2.0F) + Mth.ceil((double) absorption / 2.0F);
        heartCount = Math.min(heartCount, 10);

        for (int j = heartCount - 1; j >= 0; --j) {
            int heartValue = (j + 1) * 2;
            int row = j / 10;
            int col = j % 10;
            int xPos = x + col * 8;
            int yPos = y - row * lines;

            int yOffset = yPos - y;
            if (health + absorption <= 4) {
                yOffset += player.getRandom().nextInt(2);
            }
            if (j == regeneratingHeartIndex) {
                yOffset -= 2;
            }

            // FIX 1: Force White Color for background
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            ColorfulHeartsMixin.ott$renderHeart(guiGraphics, Gui.HeartType.CONTAINER, xPos, y, yOffset, false, false, isHardcore);

            // 2. Draw "Under" Health Layer
            if (healthDeck > 0) {
                int deckHealth = (healthDeck - 1) * 20;
                if (totalHealth > deckHealth) {
                    int color = ott$getDeckColor(healthDeck - 1);
                    ColorfulHeartsMixin.ott$renderTintedHeart(guiGraphics, xPos, y, yOffset, color, false);
                }
            }

            // 3. Draw "Current" Health Layer
            int currentDeckHealth = totalHealth - (healthDeck * 20);
            boolean isHalf = (currentDeckHealth == heartValue - 1);
            boolean isFull = (currentDeckHealth >= heartValue);

            if (isFull || isHalf) {
                if (renderHighlight) {
                    ColorfulHeartsMixin.ott$renderTintedHeart(guiGraphics, xPos, y, yOffset, 0xFFFFFF, isHalf);
                } else {
                    if (heartType != Gui.HeartType.NORMAL) {
                        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                        ColorfulHeartsMixin.ott$renderHeart(guiGraphics, heartType, xPos, y, yOffset, false, isHalf, isHardcore);
                    } else {
                        int color = ott$getDeckColor(healthDeck);
                        ColorfulHeartsMixin.ott$renderTintedHeart(guiGraphics, xPos, y, yOffset, color, isHalf);
                    }
                }
            }

            // 4. Absorption Logic
            if (totalAbsorb > 0) {
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                if (absorbDeck > 0) {
                    ColorfulHeartsMixin.ott$renderHeart(guiGraphics, Gui.HeartType.ABSORBING, xPos, y, yOffset, false, false, isHardcore);
                }
                int currentAbsorb = totalAbsorb - (absorbDeck * 20);
                if (currentAbsorb > 0) {
                    boolean absorbHalf = (currentAbsorb == heartValue - 1);
                    boolean absorbFull = (currentAbsorb >= heartValue);
                    if (absorbFull || absorbHalf) {
                        ColorfulHeartsMixin.ott$renderHeart(guiGraphics, Gui.HeartType.ABSORBING, xPos, y, yOffset, false, absorbHalf, isHardcore);
                    }
                }
            }
        }

        // Update height (Instance context)
        // Accessor is now in its own file
        GuiAccessor accessor = (GuiAccessor) this;
        accessor.setLeftHeight(accessor.getLeftHeight() + 10);
    }

    @Unique
    private static void ott$renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, int yOffset, boolean blinking, boolean halfHeart, boolean hardcore) {
        RenderSystem.enableBlend();
        ResourceLocation sprite = heartType.getSprite(hardcore, halfHeart, blinking);
        guiGraphics.blitSprite(sprite, x, y + yOffset, 9, 9);
        RenderSystem.disableBlend();
    }

    @Unique
    private static void ott$renderTintedSprite(GuiGraphics guiGraphics, ResourceLocation sprite, int x, int y, int color) {
        RenderSystem.enableBlend();
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        guiGraphics.setColor(r, g, b, 1.0F);
        guiGraphics.blitSprite(sprite, x, y, 9, 9);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    @Unique
    private static void ott$renderTintedHeart(GuiGraphics guiGraphics, int x, int y, int yOffset, int color, boolean halfHeart) {
        RenderSystem.enableBlend();
        ResourceLocation sprite = halfHeart ? WHITE_HEART_HALF : WHITE_HEART_FULL;
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        guiGraphics.setColor(r, g, b, 1.0F);
        guiGraphics.blitSprite(sprite, x, y + yOffset, 9, 9);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    @Unique
    private static int ott$getDeckColor(int deck) {
        if (deck <= 0) return 0xb02e26;
        int index = (deck - 1) % (DECK_COLORS.length - 1) + 1;
        return DECK_COLORS[index];
    }
}