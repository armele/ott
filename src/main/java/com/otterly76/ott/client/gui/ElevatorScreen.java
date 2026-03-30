package com.otterly76.ott.client.gui;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.ElevatorMenu;
import com.otterly76.ott.network.elevator.ElevatorRemoveCamoPacket;
import com.otterly76.ott.network.elevator.ElevatorSetArrowPacket;
import com.otterly76.ott.network.elevator.ElevatorSetDirectionalPacket;
import com.otterly76.ott.network.elevator.ElevatorSetFacingPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ElevatorScreen extends AbstractContainerScreen<ElevatorMenu> {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/elevator.png");

    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 120;

    public ElevatorScreen(ElevatorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        // Toggle Show Arrow button
        addRenderableWidget(Button.builder(
                getArrowButtonLabel(),
                btn -> {
                    boolean newVal = !menu.isShowArrow();
                    PacketDistributor.sendToServer(new ElevatorSetArrowPacket(menu.getBlockPos(), newVal));
                    btn.setMessage(arrowLabel(newVal));
                }
        ).bounds(x + 8, y + 20, 160, 20).build());

        // Toggle Directional button
        addRenderableWidget(Button.builder(
                getDirectionalButtonLabel(),
                btn -> {
                    boolean newVal = !menu.isDirectional();
                    PacketDistributor.sendToServer(new ElevatorSetDirectionalPacket(menu.getBlockPos(), newVal));
                    btn.setMessage(directionalLabel(newVal));
                }
        ).bounds(x + 8, y + 45, 160, 20).build());

        // Cycle Facing button (only matters when directional)
        addRenderableWidget(Button.builder(
                getFacingButtonLabel(),
                btn -> {
                    Direction next = cycleFacing(menu.getFacing());
                    PacketDistributor.sendToServer(new ElevatorSetFacingPacket(menu.getBlockPos(), next));
                    btn.setMessage(facingLabel(next));
                }
        ).bounds(x + 8, y + 70, 160, 20).build());

        // Remove Camo button
        addRenderableWidget(Button.builder(
                Component.translatable("gui.ott.elevator.remove_camo"),
                btn -> PacketDistributor.sendToServer(new ElevatorRemoveCamoPacket(menu.getBlockPos()))
        ).bounds(x + 8, y + 95, 160, 20).build());
    }

    private Component getArrowButtonLabel() {
        return arrowLabel(menu.isShowArrow());
    }

    private Component getDirectionalButtonLabel() {
        return directionalLabel(menu.isDirectional());
    }

    private Component getFacingButtonLabel() {
        return facingLabel(menu.getFacing());
    }

    private static Component arrowLabel(boolean showArrow) {
        return Component.translatable("gui.ott.elevator.arrow",
                Component.translatable(showArrow ? "gui.ott.elevator.on" : "gui.ott.elevator.off"));
    }

    private static Component directionalLabel(boolean directional) {
        return Component.translatable("gui.ott.elevator.directional",
                Component.translatable(directional ? "gui.ott.elevator.on" : "gui.ott.elevator.off"));
    }

    private static Component facingLabel(Direction facing) {
        return Component.translatable("gui.ott.elevator.facing",
                Component.translatable("gui.ott.elevator.facing." + facing.getName()));
    }

    private static Direction cycleFacing(Direction current) {
        return switch (current) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
            default -> Direction.NORTH;
        };
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Simple solid background (no custom texture required)
        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;
        graphics.fill(x, y, x + GUI_WIDTH, y + GUI_HEIGHT, 0xFF808080);
        graphics.fill(x + 1, y + 1, x + GUI_WIDTH - 1, y + GUI_HEIGHT - 1, 0xFFC6C6C6);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}