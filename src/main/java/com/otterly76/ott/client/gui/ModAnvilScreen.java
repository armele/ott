package com.otterly76.ott.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.otterly76.ott.block.entity.AnvilBlockEntity;
import com.otterly76.ott.client.gui.components.FormattableEditBox;
import com.otterly76.ott.client.gui.components.FormattingGuideWidget;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.mixin.client.AnvilScreenAccessor;
import com.otterly76.ott.network.C2SRenameItemMessage;
import com.otterly76.ott.util.data.ComponentDecomposer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ModAnvilScreen extends AnvilScreen {
    private static final Component TOO_EXPENSIVE_TEXT = Component.translatable("container.repair.expensive");

    public ModAnvilScreen(AnvilMenu anvilMenu, Inventory inventory, Component component) {
        super(anvilMenu, inventory, component);
        this.titleLabelY = 8;
    }

    protected void subInit() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        if (OttConfig.ANVILS.MISC.RENAMING_SUPPORTS_FORMATTING.get()) {
            ((AnvilScreenAccessor) this).ott$setName(new FormattableEditBox(this.font, i + 62, j + 24, 103, 12, AnvilBlockEntity.REPAIR_COMPONENT));
        } else {
            ((AnvilScreenAccessor) this).ott$setName(new EditBox(this.font, i + 62, j + 24, 103, 12, AnvilBlockEntity.REPAIR_COMPONENT));
        }

        ((AnvilScreenAccessor) this).ott$getName().setCanLoseFocus(false);
        ((AnvilScreenAccessor) this).ott$getName().setTextColor(-1);
        ((AnvilScreenAccessor) this).ott$getName().setTextColorUneditable(-1);
        ((AnvilScreenAccessor) this).ott$getName().setBordered(false);
        ((AnvilScreenAccessor) this).ott$getName().setMaxLength(50);
        ((AnvilScreenAccessor) this).ott$getName().setResponder(this::onNameChanged);
        ((AnvilScreenAccessor) this).ott$getName().setValue("");
        this.addWidget(((AnvilScreenAccessor) this).ott$getName());
        this.setInitialFocus(((AnvilScreenAccessor) this).ott$getName());
        ((AnvilScreenAccessor) this).ott$getName().setEditable(false);
        ((AnvilScreenAccessor) this).ott$getName().setVisible(false);
        this.addRenderableWidget(new FormattingGuideWidget(i + 165, j + 25, this.font));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        EditBox nameField = ((AnvilScreenAccessor) this).ott$getName();
        return this.getFocused() == nameField && this.isDragging() && button == 0 ? nameField.mouseDragged(mouseX, mouseY, button, dragX, dragY) : super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private void onNameChanged(String input) {
        Slot slot = this.menu.getSlot(0);
        if (slot.hasItem()) {
            if (!slot.getItem().has(DataComponents.CUSTOM_NAME) && input.equals(slot.getItem().getHoverName().getString())) {
                input = "";
            }

            if (this.menu.setItemName(input)) {
                PacketDistributor.sendToServer(new C2SRenameItemMessage(input));
            }
        }
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        boolean visible = ((AnvilScreenAccessor) this).ott$getName().isVisible();
        super.resize(minecraft, width, height);
        ((AnvilScreenAccessor) this).ott$getName().setVisible(visible);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        int i = this.menu.getCost();
        if (i != 0 && this.minecraft != null && this.minecraft.player != null) {
            int j = 8453920;
            int maxAnvilRepairCost = OttConfig.ANVILS.COSTS.TOO_EXPENSIVE_LIMIT.get();
            Component component;
            if ((maxAnvilRepairCost != -1 && i >= maxAnvilRepairCost || i == -1) && !this.minecraft.player.getAbilities().instabuild) {
                component = TOO_EXPENSIVE_TEXT;
                j = 16736352;
            } else if (!this.menu.getSlot(2).hasItem()) {
                component = null;
            } else {
                component = Component.translatable("container.repair.cost", i);
                if (!this.menu.getSlot(2).mayPickup(this.minecraft.player)) {
                    j = 16736352;
                }
            }

            if (component != null) {
                int k = this.imageWidth - 8 - this.font.width(component) - 2;
                guiGraphics.fill(k - 2, 67, this.imageWidth - 8, 79, 1325400064);
                guiGraphics.drawString(this.font, component, k, 69, j);
            }
        }
    }

    @Override
    public void slotChanged(@NotNull AbstractContainerMenu containerToSend, int dataSlotIndex, @NotNull ItemStack stack) {
        if (dataSlotIndex == 0) {
            ((AnvilScreenAccessor) this).ott$getName().setValue(stack.isEmpty() ? "" : ComponentDecomposer.toFormattedString(stack.getHoverName()));
            ((AnvilScreenAccessor) this).ott$getName().setEditable(!stack.isEmpty());
            this.setFocused(((AnvilScreenAccessor) this).ott$getName());
            ((AnvilScreenAccessor) this).ott$getName().setVisible(!stack.isEmpty());
        }
    }
}
