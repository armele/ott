package com.otterly76.ott.client.gui;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.WeatheringStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class WeatheringStationScreen extends AbstractContainerScreen<WeatheringStationMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/container/weathering_station.png");

    public WeatheringStationScreen(WeatheringStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
        renderFluidTank(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 73, y + 35, 176, 0, menu.getScaledProgress(), 16);
        }
    }

    private void renderFluidTank(GuiGraphics guiGraphics, int x, int y) {
        int amount = menu.getFluidAmount();
        int capacity = menu.getFluidCapacity();
        if (capacity > 0) {
            int tankHeight = 45;
            int scaledHeight = (int) ((float) amount / capacity * tankHeight);
            // Assuming tank is at (20, 20) in the GUI texture and its background is already rendered
            // Fluid overlay could be at (176, 17) in the texture
            guiGraphics.blit(TEXTURE, x + 20, y + 20 + (tankHeight - scaledHeight), 176, 17 + (tankHeight - scaledHeight), 16, scaledHeight);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}