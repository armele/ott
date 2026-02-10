package com.otterly76.ott.client.gui.components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class FormattingGuideWidget extends AbstractStringWidget {
    private static final Component QUESTION_MARK_COMPONENT = Component.literal("?");

    public FormattingGuideWidget(int x, int y, Font font) {
        super(x - font.width(QUESTION_MARK_COMPONENT) * 2, y, font.width(QUESTION_MARK_COMPONENT) * 2, 9, QUESTION_MARK_COMPONENT, font);
        this.active = true;

        MutableComponent tooltipComponent = Component.empty();
        ChatFormatting[] values = ChatFormatting.values();
        for (int i = 0; i < values.length; i++) {
            ChatFormatting chatFormatting = values[i];
            MutableComponent component = Component.translatable("chat.formatting." + chatFormatting.getName());
            if (chatFormatting != ChatFormatting.BLACK && chatFormatting != ChatFormatting.OBFUSCATED) {
                component.withStyle(chatFormatting);
            }

            tooltipComponent.append(Component.literal("§" + chatFormatting.getChar()).append(" - ").append(component));
            if (i < values.length - 1) {
                tooltipComponent.append("\n");
            }
        }
        this.setTooltip(Tooltip.create(tooltipComponent));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Integer yellowColor = ChatFormatting.YELLOW.getColor();
        this.setColor(this.isHoveredOrFocused() && yellowColor != null ? yellowColor : 4210752);
        int posX = this.getX() + (this.getWidth() - this.getFont().width(this.getMessage())) / 2;
        int posY = this.getY() + (this.getHeight() - 9) / 2;
        guiGraphics.drawString(this.getFont(), this.getMessage(), posX, posY, this.getColor(), false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
}