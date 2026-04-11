package com.otterly76.ott.client.polymorph;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.polymorph.CraftingRecipePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RecipeOutputButton extends AbstractWidget {

    private final ItemStack output;
    private final ResourceLocation recipeId;
    private final WidgetSprites normalSprites;
    private final WidgetSprites highlightedSprites;
    private boolean highlighted = false;

    public RecipeOutputButton(CraftingRecipePair pair, WidgetSprites normal, WidgetSprites highlighted) {
        super(0, 0, 25, 25, Component.empty());
        this.output = pair.output();
        this.recipeId = pair.id();
        this.normalSprites = normal;
        this.highlightedSprites = highlighted;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        WidgetSprites sprites = this.highlighted ? this.highlightedSprites : this.normalSprites;
        ResourceLocation texture = sprites.enabled();
        if (this.getX() + 25 > mouseX && this.getX() <= mouseX
                && this.getY() + 25 > mouseY && this.getY() <= mouseY) {
            texture = sprites.enabledFocused();
        }
        guiGraphics.blitSprite(texture, this.getX(), this.getY(), 600, this.width, this.height);

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, 700);
        guiGraphics.renderItem(this.output, this.getX() + 4, this.getY() + 4);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, this.output,
                this.getX() + 4, this.getY() + 4);
        poseStack.popPose();
    }

    public ResourceLocation getRecipeId() {
        return recipeId;
    }

    public ItemStack getOutput() {
        return output;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public int getWidth() {
        return 25;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }
}
