package com.otterly76.ott.client.gui;

import com.otterly76.ott.inventory.TrashMenu;
import com.otterly76.ott.network.ServerboundConfirmTrashPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class TrashScreen extends AbstractContainerScreen<TrashMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/shulker_box.png");

    public TrashScreen(TrashMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        // Standard Small Chest / Shulker Box dimensions
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        // Add "Trash It" button.
        this.addRenderableWidget(Button.builder(Component.literal("Trash It"), (button) -> {
            // 1. Tell the server to delete
            PacketDistributor.sendToServer(new ServerboundConfirmTrashPacket());

            // 2. Client-side transition: Close this and open inventory
            if (this.minecraft != null && this.minecraft.player != null) {
                // We use a custom flag-less approach to avoid the recursion crash
                this.minecraft.player.closeContainer();
                this.minecraft.setScreen(new InventoryScreen(this.minecraft.player));
            }
        }).bounds(this.leftPos + 108, this.topPos + 4, 62, 12).build());
    }

    @Override
    public void removed() {
        super.removed();
        // No logic here to avoid StackOverflow
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Standard titles, but we use "Trash Area" to be explicit
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal("Safety Return Area"), this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}