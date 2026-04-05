package com.otterly76.ott.client.gui.creative;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OttCategoryButton extends Button {

    private final OttCreativeCategories category;

    public OttCategoryButton(int x, int y, OttCreativeCategories category, OnPress onPress) {
        super(x, y, 26, 26, Component.empty(), onPress, DEFAULT_NARRATION);
        this.category = category;
        this.setTooltip(Tooltip.create(category.getDisplayName()));
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = this.getX();
        int y = this.getY();
        int w = this.getWidth();
        int h = this.getHeight();

        // Background — slightly lighter on hover
        graphics.fill(x, y, x + w, y + h, this.isHovered() ? 0xC0303030 : 0xC0101010);

        // 2-pixel amber border when this category is selected
        if (OttCreativeCategories.getSelected() == this.category) {
            int c = 0xFFFFAA00;
            graphics.fill(x,         y,         x + w,     y + 2,     c); // top
            graphics.fill(x,         y + h - 2, x + w,     y + h,     c); // bottom
            graphics.fill(x,         y + 2,     x + 2,     y + h - 2, c); // left
            graphics.fill(x + w - 2, y + 2,     x + w,     y + h - 2, c); // right
        }

        // Item icon — 16×16 centred in 26×26 → offset 5
        graphics.renderItem(new ItemStack(category.getIconItem()), x + 5, y + 5);
    }
}
