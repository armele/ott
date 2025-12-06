package com.otterly76.ott.client;

import com.otterly76.ott.Constants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FoodTooltipRenderer implements ClientTooltipComponent {
    public static final ResourceLocation HUNGER_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/sprites/tooltip_hunger_outline.png");
    public static final ResourceLocation ICONS_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/icons.png");

    private final FoodTooltip foodTooltip;

    public FoodTooltipRenderer(FoodTooltip foodTooltip) {
        this.foodTooltip = foodTooltip;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        int hunger = foodTooltip.getModifiedFood().nutrition();
        int bars = (int) Math.ceil(hunger / 2.0);
        return bars * 9;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {

        int hunger = foodTooltip.getModifiedFood().nutrition();
        int bars = (int) Math.ceil(hunger / 2.0);

        for (int i = 0; i < bars; i++) {
            int offsetX = x + (i * 8);

            guiGraphics.blit(HUNGER_SPRITE, offsetX, y, 0, 0, 9, 9, 9, 9);

            int iconU = 16;
            int iconV = 27;

            if (i * 2 + 1 < hunger) {
                iconU += 36;
            } else if (i * 2 + 1 == hunger) {
                iconU += 45;
            }

            guiGraphics.blit(ICONS_TEXTURE, offsetX, y, iconU, iconV, 9, 9, 256, 256);
        }
    }
}