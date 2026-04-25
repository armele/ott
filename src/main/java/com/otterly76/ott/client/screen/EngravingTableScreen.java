package com.otterly76.ott.client.screen;

import com.otterly76.ott.Constants;
import com.otterly76.ott.inventory.EngravingTableMenu;
import com.otterly76.ott.network.ServerboundEngraveCraftPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EngravingTableScreen extends AbstractContainerScreen<EngravingTableMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/container/engraving_table.png");

    private static final WidgetSprites SINGLE_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "single_block_button"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "single_block_button_highlighted"));
    private static final WidgetSprites HORIZONTAL_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "horizontal_blocks_button"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "horizontal_blocks_button_highlighted"));
    private static final WidgetSprites VERTICAL_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "vertical_blocks_button"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "vertical_blocks_button_highlighted"));
    private static final WidgetSprites TWO_BY_TWO_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "two_by_two_button"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "two_by_two_button_highlighted"));

    private static final int HIGHLIGHT_SELECTED = 0x70FFFF00;
    private static final int HIGHLIGHT_MATCH    = 0x700000FF;
    private static final int HIGHLIGHT_DIM      = 0x70000000;

    private BlockPreviewWidget.Mode previewMode = BlockPreviewWidget.Mode.TWO_BY_TWO;
    private EditBox searchBox;
    private double scrollAmount;
    private GridLayout grid;
    private final List<EngravingSlotWidget> slotWidgets = new ArrayList<>();

    // Scissor bounds for the result grid (screen-relative, set in renderBackground)
    private int gridLeft, gridTop;

    public EngravingTableScreen(EngravingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth  = 256;
        this.imageHeight = 256;
        this.titleLabelX = 88;
        this.titleLabelY = 14;
        this.inventoryLabelX = 88;
        this.inventoryLabelY = 155;
    }

    @Override
    protected void init() {
        super.init();

        gridLeft = leftPos + 85;
        gridTop  = topPos  + 40;

        searchBox = addRenderableWidget(new EditBox(font, leftPos + 105, topPos + 27, 115, 11, Component.empty()));
        searchBox.setTextColor(-1);
        searchBox.setTextColorUneditable(-1);
        searchBox.setBordered(false);
        searchBox.setMaxLength(50);
        searchBox.setResponder(this::onSearchChanged);
        searchBox.setEditable(false);

        addRenderableWidget(Button.builder(Component.translatable("text.ott.engrave.craft"), btn -> craft())
                .bounds(leftPos + 9, topPos + 101, 72, 18)
                .build());

        addRenderableWidget(new ImageButton(leftPos + 9,  topPos + 121, 18, 18,
                SINGLE_SPRITES,    btn -> previewMode = BlockPreviewWidget.Mode.SINGLE_BLOCK))
                .setTooltip(Tooltip.create(Component.translatable("text.ott.engrave.single")));
        addRenderableWidget(new ImageButton(leftPos + 27, topPos + 121, 18, 18,
                HORIZONTAL_SPRITES, btn -> previewMode = BlockPreviewWidget.Mode.HORIZONTAL_BLOCK))
                .setTooltip(Tooltip.create(Component.translatable("text.ott.engrave.horizontal")));
        addRenderableWidget(new ImageButton(leftPos + 45, topPos + 121, 18, 18,
                VERTICAL_SPRITES,  btn -> previewMode = BlockPreviewWidget.Mode.VERTICAL_BLOCK))
                .setTooltip(Tooltip.create(Component.translatable("text.ott.engrave.vertical")));
        addRenderableWidget(new ImageButton(leftPos + 63, topPos + 121, 18, 18,
                TWO_BY_TWO_SPRITES, btn -> previewMode = BlockPreviewWidget.Mode.TWO_BY_TWO))
                .setTooltip(Tooltip.create(Component.translatable("text.ott.engrave.two_by_two")));

        addRenderableWidget(new BlockPreviewWidget(leftPos + 9, topPos + 26, 72, 72,
                () -> previewMode, this::previewState));

        addSlotWidgets();
    }

    private void addSlotWidgets() {
        slotWidgets.forEach(this::removeWidget);
        slotWidgets.clear();

        var results = menu.results();
        int rows = Math.max(6, Mth.ceil(results.size() / 9f));

        grid = new GridLayout(gridLeft, gridTop);
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < rows; row++) {
                int index = col + row * 9;
                ItemStack stack = results.size() > index ? results.get(index) : ItemStack.EMPTY;
                EngravingSlotWidget slot = addWidget(new EngravingSlotWidget(stack, menu, gridTop, gridTop + 109));
                grid.addChild(slot, row, col);
                slotWidgets.add(slot);
            }
        }
        grid.arrangeElements();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        grid.setY(gridTop - (int) scrollAmount);

        // Scissored result grid area
        int scissorX = leftPos + 84;
        int scissorY = topPos  + 40;
        int scissorW = 163;
        int scissorH = 109;
        graphics.enableScissor(scissorX, scissorY, scissorX + scissorW, scissorY + scissorH);
        for (EngravingSlotWidget widget : slotWidgets) {
            widget.renderWidget(graphics, mouseX, mouseY, partialTick);
        }
        graphics.disableScissor();

        for (EngravingSlotWidget widget : slotWidgets) {
            widget.renderTooltip(graphics, font, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Slot highlights in the player inventory
        ItemStack selected = menu.selectedStack();
        if (!selected.isEmpty()) {
            for (Slot slot : menu.slots) {
                int sx = slot.x + leftPos - 1;
                int sy = slot.y + topPos  - 1;
                if (ItemStack.isSameItemSameComponents(selected, slot.getItem()) || (ItemStack.isSameItem(selected, slot.getItem()) && hasShiftDown())) {
                    graphics.fill(sx, sy, sx + 18, sy + 18, HIGHLIGHT_SELECTED);
                } else if (ItemStack.isSameItem(selected, slot.getItem())) {
                    graphics.fill(sx, sy, sx + 18, sy + 18, HIGHLIGHT_MATCH);
                } else {
                    graphics.fill(sx, sy, sx + 18, sy + 18, HIGHLIGHT_DIM);
                }
            }
        }
    }

    private void onSearchChanged(String filter) {
        scrollAmount = 0;
        menu.updateResults(filter);
        addSlotWidgets();
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        addSlotWidgets();
        searchBox.setEditable(!menu.selectedStack().isEmpty());
        scrollAmount = 0;
        menu.setFilter(searchBox.getValue());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            onClose();
            return true;
        }
        if (getFocused() == searchBox) {
            return searchBox.keyPressed(keyCode, scanCode, modifiers)
                    || searchBox.canConsumeInput()
                    || super.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (getFocused() != searchBox) setFocused(null);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (menu.results().size() <= 54) return false;
        setScrollAmount(scrollAmount - scrollY * 16 / 2.0);
        return true;
    }

    private void setScrollAmount(double amount) {
        int rows = Mth.ceil(menu.results().size() / 9f);
        scrollAmount = Mth.clamp(amount, 0, rows * 18 - 108);
    }

    private void craft() {
        ItemStack chosen = menu.chosenStack();
        if (!menu.selectedStack().isEmpty() && !chosen.isEmpty()) {
            PacketDistributor.sendToServer(new ServerboundEngraveCraftPacket(chosen, hasShiftDown()));
            menu.reset();
            addSlotWidgets();
            scrollAmount = 0;
            setFocused(null);
            searchBox.setEditable(false);
            searchBox.setValue("");
        }
    }

    private @Nullable BlockState previewState() {
        Block block = Block.byItem(menu.chosenStack().getItem());
        BlockState state = block.defaultBlockState();
        return state.isAir() ? null : state;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Let slot widgets handle clicks before the container screen
        for (EngravingSlotWidget widget : slotWidgets) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                Objects.requireNonNull(minecraft).getSoundManager()
                        .play(net.minecraft.client.resources.sounds.SimpleSoundInstance
                                .forUI(net.minecraft.sounds.SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1f));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}