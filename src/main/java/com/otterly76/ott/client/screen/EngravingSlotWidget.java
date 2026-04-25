package com.otterly76.ott.client.screen;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.EngravingTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EngravingSlotWidget extends AbstractWidget {

    public static final ResourceLocation SLOT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/sprites/engraving_slot.png");

    private final ItemStack stack;
    private final EngravingTableMenu menu;
    private final int minY;
    private final int maxY;

    public EngravingSlotWidget(ItemStack stack, EngravingTableMenu menu, int minY, int maxY) {
        super(0, 0, 18, 18, CommonComponents.EMPTY);
        this.stack = stack;
        this.menu = menu;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blit(SLOT_TEXTURE, getX(), getY(), 0, 0, 18, 18, 18, 18);

        if (isMouseOver(mouseX, mouseY)) {
            graphics.fill(getX() + 1, getY() + 1, getX() + 17, getY() + 17, 0x80FFFFFF);
        }
        graphics.renderItem(stack, getX() + 1, getY() + 1);
    }

    public void renderTooltip(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY) && !stack.isEmpty()) {
            graphics.renderTooltip(font, Screen.getTooltipFromItem(Minecraft.getInstance(), stack),
                    stack.getTooltipImage(), mouseX, mouseY);
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY) && mouseY >= minY && mouseY <= maxY;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY) && !stack.isEmpty()) {
            menu.setChosenStack(stack);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narration) {}
}
