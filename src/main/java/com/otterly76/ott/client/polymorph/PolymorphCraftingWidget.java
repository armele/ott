package com.otterly76.ott.client.polymorph;

import com.mojang.blaze3d.systems.RenderSystem;
import com.otterly76.ott.Constants;
import com.otterly76.ott.mixin.client.AbstractContainerScreenAccessor;
import com.otterly76.ott.network.polymorph.ServerboundSelectCraftingRecipePacket;
import com.otterly76.ott.polymorph.CraftingRecipePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PolymorphCraftingWidget {

    private static final WidgetSprites SELECTOR_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "polymorph_selector"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "polymorph_selector_highlighted"));
    private static final WidgetSprites OUTPUT_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "polymorph_output"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "polymorph_output_highlighted"));
    private static final WidgetSprites CURRENT_OUTPUT_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "polymorph_current_output"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "polymorph_current_output_highlighted"));

    private final CraftingScreen screen;
    private final Slot outputSlot;
    private final ImageButton toggleButton;
    private final List<RecipeOutputButton> outputButtons = new ArrayList<>();
    private boolean selectionActive = false;

    public PolymorphCraftingWidget(CraftingScreen screen, Slot outputSlot) {
        this.screen = screen;
        this.outputSlot = outputSlot;
        this.toggleButton = new ImageButton(0, 0, 16, 16, SELECTOR_SPRITES,
                btn -> selectionActive = !selectionActive);
        this.toggleButton.visible = false;
    }

    public void setRecipesList(List<CraftingRecipePair> recipes, @Nullable ResourceLocation selected) {
        outputButtons.clear();
        selectionActive = false;

        for (CraftingRecipePair pair : recipes) {
            if (!pair.output().isEmpty()) {
                outputButtons.add(new RecipeOutputButton(pair, OUTPUT_SPRITES, CURRENT_OUTPUT_SPRITES));
            }
        }
        toggleButton.visible = outputButtons.size() > 1;
        if (selected != null) highlightRecipe(selected);
        updateButtonPositions();
    }

    private void highlightRecipe(ResourceLocation id) {
        for (RecipeOutputButton btn : outputButtons) {
            btn.setHighlighted(btn.getRecipeId().equals(id));
        }
    }

    private void updateTogglePosition() {
        var acc = (AbstractContainerScreenAccessor) screen;
        toggleButton.setX(acc.getLeftPos() + outputSlot.x);
        toggleButton.setY(acc.getTopPos() + outputSlot.y - 22);
    }

    private void updateButtonPositions() {
        var acc = (AbstractContainerScreenAccessor) screen;
        int size = outputButtons.size();
        int centerX = acc.getLeftPos() + outputSlot.x - 4;
        int y = acc.getTopPos() + outputSlot.y - 38;
        int xOffset = (int) (-25 * Math.floor(size / 2.0f));
        if (size % 2 == 0) xOffset += 13;

        int x = centerX + xOffset;
        for (RecipeOutputButton btn : outputButtons) {
            btn.setPosition(x, y);
            x += 25;
        }
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        updateTogglePosition();
        updateButtonPositions();

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        toggleButton.render(guiGraphics, mouseX, mouseY, partialTick);

        if (selectionActive) {
            com.mojang.blaze3d.vertex.PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate(0, 0, 500);
            for (RecipeOutputButton btn : outputButtons) {
                btn.render(guiGraphics, mouseX, mouseY, partialTick);
            }
            // Tooltip for hovered button (z=501)
            pose.translate(0, 0, 1);
            for (RecipeOutputButton btn : outputButtons) {
                if (btn.isHoveredOrFocused()) {
                    guiGraphics.renderTooltip(Minecraft.getInstance().font, btn.getOutput(), mouseX, mouseY);
                    break;
                }
            }
            pose.popPose();
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (toggleButton.visible && toggleButton.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (selectionActive) {
            for (RecipeOutputButton btn : outputButtons) {
                if (btn.mouseClicked(mouseX, mouseY, button)) {
                    selectRecipe(btn.getRecipeId());
                    selectionActive = false;
                    return true;
                }
            }
            // Click outside popup closes it
            selectionActive = false;
            return true;
        }
        return false;
    }

    private void selectRecipe(ResourceLocation recipeId) {
        PacketDistributor.sendToServer(new ServerboundSelectCraftingRecipePacket(recipeId));
        highlightRecipe(recipeId);
    }

}