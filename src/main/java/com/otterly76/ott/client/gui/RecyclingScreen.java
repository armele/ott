package com.otterly76.ott.client.gui;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.RecyclingMenu;
import com.otterly76.ott.network.recycling.ServerboundRecycleButtonClickPacket;
import com.otterly76.ott.network.recycling.ServerboundRecipePagePacket;
import com.otterly76.ott.network.recycling.ServerboundRecipeSelectPacket;
import com.otterly76.ott.recycling.RecyclingRecipe;
import com.otterly76.ott.recycling.RecyclingStatus;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RecyclingScreen extends AbstractContainerScreen<RecyclingMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/recycling_gui.png");
    private static final ResourceLocation RECIPE_PANEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/recycling_recipe_panel.png");
    private static final ResourceLocation EXP_ICON =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/sprites/recycling_exp.png");

    private List<RecyclingRecipe> recipes = List.of();
    private int selectedRecipe = 0;
    private int page = 0;
    private int recipeSize = 0;
    private static final int MAX_PAGE_SIZE = 7;

    public RecyclingScreen(RecyclingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();
        // Push leftPos right so the recipe panel (152px wide) can fit to the left
        this.leftPos = Math.max((width - imageWidth) / 2, 152 + 8);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        clearWidgets();
        init();

        renderRecycleButton(guiGraphics, mouseX, mouseY, partialTick);
        renderExpRequired(guiGraphics, mouseX, mouseY);
        renderRecipeSelection(guiGraphics, mouseX, mouseY, partialTick);
        renderInputSlotOverlay(guiGraphics);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }

    private void renderRecycleButton(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int buttonX = leftPos + (imageWidth - 64) - 20;
        int buttonY = topPos + 72;
        Button recycleBtn = Button.builder(
                Component.translatable("screen.ott.recycling.recycle"),
                this::onRecyclePressed
        ).pos(buttonX, buttonY).size(64, 16).build();
        this.addRenderableWidget(recycleBtn).render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void onRecyclePressed(Button button) {
        PacketDistributor.sendToServer(new ServerboundRecycleButtonClickPacket(hasShiftDown()));
    }

    private void renderExpRequired(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
        guiGraphics.pose().translate((this.leftPos + imageWidth - 17) * 2.0f, (this.topPos + 72) * 2.0f, 0f);
        guiGraphics.blit(EXP_ICON, 0, 0, 0, 0, 16, 16, 16, 16);
        guiGraphics.pose().popPose();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.75f, 0.75f, 0.75f);
        guiGraphics.pose().translate((this.leftPos + imageWidth - 12) * 1.3334f, (this.topPos + 82) * 1.3334f, 0f);
        drawCentered(guiGraphics, this.font, Component.literal(this.menu.getExpAmount() + ""), 0, 0, 0xFF00AA00);
        guiGraphics.pose().popPose();

        if (mouseX >= (this.leftPos + imageWidth - 19) && mouseX <= (this.leftPos + imageWidth - 7)
                && mouseY >= this.topPos + 72 && mouseY <= this.topPos + 87) {
            String expType = this.menu.getExpType().toLowerCase();
            Component exp = Component.translatable("screen.ott.recycling.exp_" + expType + "_required", this.menu.getExpAmount());
            guiGraphics.renderTooltip(this.font, exp, mouseX, mouseY);
        }
    }

    private void renderRecipeSelection(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int maxPageCount = (int) Math.ceil((double) recipeSize / MAX_PAGE_SIZE);
        int pageToDisplay = recipes.isEmpty() ? 0 : page + 1;

        if (page > maxPageCount - 1) page = 0;

        guiGraphics.blit(RECIPE_PANEL_TEXTURE, this.leftPos - 152, this.topPos, 0, 0, 152, 184, 152, 184);
        drawCentered(guiGraphics, font,
                Component.translatable("screen.ott.recycling.recipe_selection"),
                this.leftPos - 76, this.topPos + 7, 0xFF404040);
        drawCentered(guiGraphics, font,
                Component.translatable("screen.ott.recycling.page", pageToDisplay, maxPageCount),
                this.leftPos - 76, this.topPos + imageHeight - 18, 0xFF404040);

        renderNavigationButtons(guiGraphics, mouseX, mouseY, partialTick, maxPageCount);
        renderRecipeButtons(guiGraphics, mouseX, mouseY);

        if (selectedRecipe >= recipes.size()) selectedRecipe = 0;
    }

    private void renderNavigationButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int maxPageCount) {
        Button prevBtn = Button.builder(Component.literal("<"), btn -> {
            if (this.page > 0) this.page--;
            else this.page = Math.max(maxPageCount - 1, 0);
            PacketDistributor.sendToServer(new ServerboundRecipePagePacket(this.page));
        }).pos(this.leftPos - 152 + 5, this.topPos + imageHeight - 23).size(16, 16).build();
        this.addRenderableWidget(prevBtn).render(guiGraphics, mouseX, mouseY, partialTick);

        Button nextBtn = Button.builder(Component.literal(">"), btn -> {
            if (this.page < maxPageCount - 1) this.page++;
            else this.page = 0;
            PacketDistributor.sendToServer(new ServerboundRecipePagePacket(this.page));
        }).pos(this.leftPos - 21, this.topPos + imageHeight - 23).size(16, 16).build();
        this.addRenderableWidget(nextBtn).render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderRecipeButtons(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int recipeWidth = 9 * 16 + 5;
        int visibleCount = 0;

        for (int j = 0; j < recipes.size() && visibleCount < MAX_PAGE_SIZE; j++) {
            RecyclingRecipe recipe = recipes.get(j);
            int displayIndex = visibleCount;
            int btnX = this.leftPos - recipeWidth;
            int btnY = this.topPos + (displayIndex * 18) + 30;
            int btnW = recipeWidth - 3;
            int btnH = 18;

            // Highlight selected
            if (selectedRecipe == j) {
                guiGraphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, 0x40FFFFFF);
            } else if (mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH) {
                guiGraphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, 0x20FFFFFF);
            }

            // Draw items
            Map<Item, Integer> itemCounts = new HashMap<>();
            Map<Item, DataComponentMap> itemComponents = new HashMap<>();
            for (ItemStack stack : recipe.getOutputs()) {
                itemCounts.merge(stack.getItem(), stack.getCount(), Integer::sum);
                itemComponents.put(stack.getItem(), stack.getComponents());
            }

            int i = 0;
            for (Map.Entry<Item, Integer> entry : itemCounts.entrySet()) {
                if (entry.getKey() == Items.AIR) continue;
                ItemStack displayStack = new ItemStack(entry.getKey(), entry.getValue());
                if (itemComponents.containsKey(entry.getKey())) {
                    displayStack.applyComponents(itemComponents.get(entry.getKey()));
                }
                int itemX = this.leftPos - recipeWidth + (i * 16) + 1;
                int itemY = this.topPos + (displayIndex * 18) + 31;
                guiGraphics.renderFakeItem(displayStack, itemX, itemY);
                guiGraphics.renderItemDecorations(this.font, displayStack, itemX, itemY);
                if (mouseX >= itemX && mouseX <= itemX + 16 && mouseY >= itemY && mouseY <= itemY + 16) {
                    guiGraphics.renderTooltip(this.font, displayStack, mouseX, mouseY);
                }
                i++;
            }
            visibleCount++;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && !recipes.isEmpty()) {
            int recipeWidth = 9 * 16 + 5;
            for (int j = 0; j < recipes.size() && j < MAX_PAGE_SIZE; j++) {
                int btnX = this.leftPos - recipeWidth;
                int btnY = this.topPos + (j * 18) + 30;
                int btnW = recipeWidth - 3;
                int btnH = 18;
                if (mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH) {
                    selectedRecipe = j;
                    PacketDistributor.sendToServer(new ServerboundRecipeSelectPacket(recipes.get(j)));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderInputSlotOverlay(GuiGraphics guiGraphics) {
        RecyclingStatus status = RecyclingStatus.byIndex(this.menu.getStatus());
        if (status != RecyclingStatus.BLANK) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 300);
            guiGraphics.fill(this.leftPos + 25, this.topPos + 34, this.leftPos + 43, this.topPos + 52,
                    FastColor.ARGB32.color(70, status.getOverlay()));
            guiGraphics.pose().popPose();
        }
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
        RecyclingStatus status = RecyclingStatus.byIndex(this.menu.getStatus());
        if (this.hoveredSlot != null && status != RecyclingStatus.BLANK && this.hoveredSlot.index == 0) {
            ItemStack itemStack = this.hoveredSlot.getItem();
            List<Component> tooltip = new ArrayList<>(this.getTooltipFromContainerItem(itemStack));
            tooltip.add(Component.translatable(status.getTranslationKey()).withColor(status.getOverlay()));
            guiGraphics.renderTooltip(this.font, tooltip, itemStack.getTooltipImage(), itemStack, x, y);
        } else {
            super.renderTooltip(guiGraphics, x, y);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollDelta) {
        if (mouseX >= this.leftPos - 152 && mouseX <= this.leftPos && mouseY >= this.topPos && mouseY <= this.topPos + 184) {
            int maxPageCount = (int) Math.ceil((double) recipeSize / MAX_PAGE_SIZE);
            if (scrollDelta == 1.0d && this.page > 0) {
                this.page--;
            } else if (scrollDelta == -1.0d && (this.page + 1) * MAX_PAGE_SIZE < recipeSize) {
                this.page++;
            } else if (scrollDelta == 1.0d && this.page == 0 && !recipes.isEmpty()) {
                this.page = maxPageCount - 1;
            } else if (scrollDelta == -1.0d && (this.page + 1) * MAX_PAGE_SIZE >= recipeSize) {
                this.page = 0;
            }
            PacketDistributor.sendToServer(new ServerboundRecipePagePacket(this.page));
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollDelta);
    }

    /** Called from ClientPayloadHandler when the server sends a recipe list update. */
    public void updateRecipes(List<RecyclingRecipe> newRecipes, int size, boolean shouldSendPacket) {
        this.recipes = newRecipes;
        this.recipeSize = size;

        if (size < 7 && this.page != 0) {
            this.page = 0;
            PacketDistributor.sendToServer(new ServerboundRecipePagePacket(0));
        }

        if (!recipes.isEmpty()) {
            if (selectedRecipe >= this.recipes.size()) selectedRecipe = 0;
            if (shouldSendPacket) {
                PacketDistributor.sendToServer(new ServerboundRecipeSelectPacket(this.recipes.get(selectedRecipe)));
            }
        }
    }

    /** Called from ClientPayloadHandler on recipe-select-request: re-send current selection. */
    public void resubmitSelection() {
        if (!recipes.isEmpty()) {
            if (selectedRecipe >= recipes.size()) selectedRecipe = 0;
            PacketDistributor.sendToServer(new ServerboundRecipeSelectPacket(recipes.get(selectedRecipe)));
        }
    }

    private void drawCentered(GuiGraphics guiGraphics, Font font, Component text, int centerX, int y, int color) {
        List<FormattedCharSequence> lines = font.split(text, 140);
        int lineHeight = font.lineHeight + 2;
        for (int i = 0; i < lines.size(); i++) {
            FormattedCharSequence line = lines.get(i);
            int lineX = centerX - font.width(line) / 2;
            guiGraphics.drawString(font, line, lineX, y + i * lineHeight, color, false);
        }
    }
}
