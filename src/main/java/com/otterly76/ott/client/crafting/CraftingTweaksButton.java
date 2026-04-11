package com.otterly76.ott.client.crafting;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CraftingTweaksButton extends AbstractWidget {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("ott", "textures/gui/crafting_tweaks.png");

    private final int normalTexX;
    private final int normalTexY;
    private final int hoverTexX;
    private final int hoverTexY;
    private final int altNormalTexX;
    private final int altNormalTexY;
    private final int altHoverTexX;
    private final int altHoverTexY;
    private final String tooltipKey;
    private final String altTooltipKey;
    private final Consumer<Boolean> onClick;

    public CraftingTweaksButton(int x, int y,
                                int normalTexX, int normalTexY,
                                int hoverTexX, int hoverTexY,
                                int altNormalTexX, int altNormalTexY,
                                int altHoverTexX, int altHoverTexY,
                                String tooltipKey, String altTooltipKey,
                                Consumer<Boolean> onClick) {
        super(x, y, 18, 18, Component.empty());
        this.normalTexX = normalTexX;
        this.normalTexY = normalTexY;
        this.hoverTexX = hoverTexX;
        this.hoverTexY = hoverTexY;
        this.altNormalTexX = altNormalTexX;
        this.altNormalTexY = altNormalTexY;
        this.altHoverTexX = altHoverTexX;
        this.altHoverTexY = altHoverTexY;
        this.tooltipKey = tooltipKey;
        this.altTooltipKey = altTooltipKey;
        this.onClick = onClick;
    }

    public static CraftingTweaksButton rotate(int x, int y, Runnable onRotate, Runnable onRotateCcw) {
        return new CraftingTweaksButton(x, y,
                16, 0, 16, 16,
                64, 0, 64, 16,
                "tooltip.ott.crafting.rotate", "tooltip.ott.crafting.rotate_ccw",
                isAlt -> { if (isAlt) onRotateCcw.run(); else onRotate.run(); });
    }

    public static CraftingTweaksButton balance(int x, int y, Runnable onBalance, Runnable onSpread) {
        return new CraftingTweaksButton(x, y,
                48, 0, 48, 16,
                96, 0, 96, 16,
                "tooltip.ott.crafting.balance", "tooltip.ott.crafting.spread",
                isAlt -> { if (isAlt) onSpread.run(); else onBalance.run(); });
    }

    public static CraftingTweaksButton clear(int x, int y, Runnable onClear, Runnable onForceClear) {
        return new CraftingTweaksButton(x, y,
                32, 0, 32, 16,
                80, 0, 80, 16,
                "tooltip.ott.crafting.clear", "tooltip.ott.crafting.force_clear",
                isAlt -> { if (isAlt) onForceClear.run(); else onClear.run(); });
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean alt = Screen.hasShiftDown();
        boolean hovered = isHovered();

        setTooltip(Tooltip.create(Component.translatable(alt ? altTooltipKey : tooltipKey)));

        int texX = alt ? (hovered ? altHoverTexX : altNormalTexX) : (hovered ? hoverTexX : normalTexX);
        int texY = alt ? (hovered ? altHoverTexY : altNormalTexY) : (hovered ? hoverTexY : normalTexY);

        guiGraphics.blit(TEXTURE, getX() + 1, getY() + 1, texX, texY, 16, 16);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isActive() || !isHovered()) return false;
        if (button != 0) return false;
        playDownSound(net.minecraft.client.Minecraft.getInstance().getSoundManager());
        onClick.accept(Screen.hasShiftDown());
        return true;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }
}
