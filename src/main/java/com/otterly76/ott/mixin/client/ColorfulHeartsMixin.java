package com.otterly76.ott.mixin.client;

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

    @Unique
    private static final int[] DECK_COLORS = new int[]{
            0xff0000, // Deck 0: Red 0-20
            0xff5300, // Deck 1: mid-Red-Orange 21-30
            0xffa500, // Deck 2: Orange 31-40
            0xffd200, // Deck 3: mid-Orange-Yellow 41-50
            0xffff00, // Deck 4: Yellow 51-60
            0x80ff00, // Deck 5: mid-Yellow-Lime 61-70
            0x00ff00, // Deck 6: Lime 71-80
            0x00c000, // Deck 7: mid-Lime-Green 81-90
            0x008000, // Deck 8: Green 91-100
            0x00c080, // Deck 9: mid-Green-Cyan 101-110
            0x00ffff, // Deck 10: Cyan 111-120
            0x00c0c0, // Deck 11: mid-Cyan-Light Blue 121-130
            0x00ffff, // Deck 12: Light Blue 131-140
            0x0080ff, // Deck 13: mid-Light Blue-Blue 141-150
            0x0000ff, // Deck 14: Blue 151-160
            0x4000c0, // Deck 15: mid-Blue-Purple 161-170
            0x800080, // Deck 16: Purple 171-180
            0xc000c0, // Deck 17: mid-Purple-Magenta 181-190
            0xff00ff, // Deck 18: Magenta 191-200
            0xff60e5, // Deck 19: mid-Magenta-Pink 201-210
            0xffc0cb, // Deck 20: Pink 211-220
            0xff6066 // Deck 21: mid-Pink-Red 221-230
            // TODO might need more, will check
    };

    @Unique
    private static final ResourceLocation WHITE_HEART_FULL = ResourceLocation.fromNamespaceAndPath("ott", "hud/heart/white_full");
    @Unique
    private static final ResourceLocation WHITE_HEART_HALF = ResourceLocation.fromNamespaceAndPath("ott", "hud/heart/white_half");

    @SuppressWarnings("resource")
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

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            ColorfulHeartsMixin.ott$renderHeart(guiGraphics, Gui.HeartType.CONTAINER, xPos, y, yOffset, false, false, isHardcore);

            if (healthDeck > 0) {
                int deckHealth = (healthDeck - 1) * 20;
                if (totalHealth > deckHealth) {
                    int color = ott$getDeckColor(healthDeck - 1);
                    ColorfulHeartsMixin.ott$renderTintedHeart(guiGraphics, xPos, y, yOffset, color, false);
                }
            }

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

        GuiAccessor accessor = (GuiAccessor) this;
        accessor.setLeftHeight(accessor.getLeftHeight() + 10);
    }

    @Unique
    @SuppressWarnings("SameParameterValue")
    private static void ott$renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, int yOffset, boolean blinking, boolean halfHeart, boolean hardcore) {
        RenderSystem.enableBlend();
        ResourceLocation sprite = heartType.getSprite(hardcore, halfHeart, blinking);
        guiGraphics.blitSprite(sprite, x, y + yOffset, 9, 9);
        RenderSystem.disableBlend();
    }

    @Unique
    @SuppressWarnings("DuplicatedCode")
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