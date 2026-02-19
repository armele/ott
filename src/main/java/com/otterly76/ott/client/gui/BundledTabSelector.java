package com.otterly76.ott.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.otterly76.ott.Constants;
import com.otterly76.ott.client.registries.ModBundledTabs;
import com.otterly76.ott.ModCreativeTabs;
import com.otterly76.ott.mixin.client.AbstractContainerScreenAccessor;
import com.otterly76.ott.mixin.client.CreativeModeInventoryScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.*;
import java.util.function.Consumer;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class BundledTabSelector {
    private static final ResourceLocation SELECTOR_BAR = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/bundled_tabs/interface.png");
    private static final int VISIBLE_CATEGORIES = 5;
    private static BundledTabSelector instance;
    private int guiLeft;
    private int guiTop;
    private int scroll;
    private AbstractWidget scrollUpButton;
    private AbstractWidget scrollDownButton;
    private List<BundledTabs> bundles = null;
    private CreativeModeTab lastTab;
    private int itemCount;

    public static BundledTabSelector bootstrap() {
        if (instance == null) {
            instance = new BundledTabSelector();
        }
        return instance;
    }

    private BundledTabSelector() {
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen creativeScreen) {
            BundledTabSelector selector = bootstrap();
            if (selector.bundles == null) {
                selector.bundles = new ArrayList<>(ModBundledTabs.getTabs());
            }

            selector.guiLeft = ((AbstractContainerScreenAccessor) creativeScreen).getLeftPos();
            selector.guiTop = ((AbstractContainerScreenAccessor) creativeScreen).getTopPos();
            selector.injectWidgets(creativeScreen, event::addListener);
            selector.itemCount = ModCreativeTabs.VANILLA_BACKPORT.get().getDisplayItems().size();
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ContainerScreenEvent.Render.Background event) {
        if (event.getContainerScreen() instanceof CreativeModeInventoryScreen creativeScreen) {
            BundledTabSelector selector = bootstrap();
            CreativeModeTab tab = CreativeModeInventoryScreenAccessor.getSelectedTab();
            GuiGraphics graphics = event.getGuiGraphics();
            graphics.pose().pushPose();
            if (selector.isValidTab(tab)) {
                graphics.blit(SELECTOR_BAR, selector.guiLeft - 30, selector.guiTop + 2, 0, 0, 30, 120);
                if (selector.hasSelectedBundle() && ((CreativeModeInventoryScreen.ItemPickerMenu) creativeScreen.getMenu()).items.size() == selector.itemCount) {
                    selector.bundles.forEach(BundledTabs::deselect);
                }
            }

            if (selector.lastTab != tab) {
                selector.onSwitchCreativeTab(tab, creativeScreen);
                selector.lastTab = tab;
            }
            graphics.pose().popPose();
        }
    }

    @SubscribeEvent
    public static void onScreenClosing(ScreenEvent.Closing event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen) {
            BundledTabSelector selector = bootstrap();
            selector.scrollUpButton = null;
            selector.scrollDownButton = null;
            if (selector.bundles != null) {
                selector.bundles.forEach((bundle) -> {
                    bundle.setContentTab(null);
                    bundle.deselect();
                });
            }
        }
    }

    private boolean hasSelectedBundle() {
        return this.bundles != null && this.bundles.stream().anyMatch(BundledTabs::isSelected);
    }

    private void injectWidgets(CreativeModeInventoryScreen screen, Consumer<AbstractWidget> widgets) {
        this.bundles.forEach((category) -> {
            Tab tab = new Tab(this.guiLeft - 23, this.guiTop + 7, category, (button) -> {
                if (category.isSelected()) {
                    category.deselect();
                } else {
                    this.bundles.forEach(BundledTabs::deselect);
                    category.select();
                }
                this.updateItems(screen);
            });
            tab.visible = false;
            widgets.accept(tab);
        });
        this.scrollUpButton = new ScrollButton(this.guiLeft - 24, this.guiTop + 6, 32, (b) -> {
            if (this.scroll > 0) {
                --this.scroll;
            }
            this.updateWidgets();
        });
        this.scrollDownButton = new ScrollButton(this.guiLeft - 24, this.guiTop + 108, 52, (b) -> {
            if (this.scroll < this.getMaxScroll()) {
                ++this.scroll;
            }
            this.updateWidgets();
        });
        widgets.accept(this.scrollUpButton);
        widgets.accept(this.scrollDownButton);
        this.updateWidgets();
        this.onSwitchCreativeTab(CreativeModeInventoryScreenAccessor.getSelectedTab(), screen);
    }

    private int getMaxScroll() {
        return Math.max(0, this.bundles.size() - VISIBLE_CATEGORIES);
    }

    private void updateItems(CreativeModeInventoryScreen screen) {
        Set<ItemStack> seen = new HashSet<>();
        LinkedHashSet<ItemStack> display = new LinkedHashSet<>();
        boolean hasSelection = this.hasSelectedBundle();
        ModCreativeTabs.VANILLA_BACKPORT.get().getDisplayItems().forEach((stack) -> {
            if (!hasSelection) {
                if (seen.add(stack)) {
                    display.add(stack.copy());
                }
            } else {
                this.bundles.stream().filter(BundledTabs::isSelected).filter((bundle) -> bundle.contains(stack)).findFirst().ifPresent((bundle) -> {
                    if (seen.add(stack)) {
                        display.add(stack.copy());
                    }
                });
            }
        });
        NonNullList<ItemStack> items = ((CreativeModeInventoryScreen.ItemPickerMenu)screen.getMenu()).items;
        items.clear();
        items.addAll(display);
        ((CreativeModeInventoryScreen.ItemPickerMenu)screen.getMenu()).scrollTo(0.0F);
    }

    private void updateWidgets() {
        this.bundles.forEach((bundle) -> bundle.setVisible(false));

        for (int i = this.scroll; i < this.scroll + VISIBLE_CATEGORIES && i < this.bundles.size(); ++i) {
            BundledTabs bundle = this.bundles.get(i);
            bundle.setY(this.guiTop + 18 * (i - this.scroll) + 18);
            bundle.setVisible(true);
        }

        boolean isValidTab = this.isValidTab(CreativeModeInventoryScreenAccessor.getSelectedTab());
        if (this.scrollUpButton != null) this.scrollUpButton.visible = isValidTab && this.scroll > 0;
        if (this.scrollDownButton != null) this.scrollDownButton.visible = isValidTab && this.scroll < this.getMaxScroll();
    }

    private void onSwitchCreativeTab(CreativeModeTab tab, CreativeModeInventoryScreen screen) {
        if (this.isValidTab(tab)) {
            this.updateWidgets();
            this.updateItems(screen);
        } else {
            if (this.scrollUpButton != null) this.scrollUpButton.visible = false;
            if (this.scrollDownButton != null) this.scrollDownButton.visible = false;
            this.bundles.forEach((bundle) -> bundle.setVisible(false));
        }
    }

    private boolean isValidTab(CreativeModeTab tab) {
        return tab == ModCreativeTabs.VANILLA_BACKPORT.get();
    }

    public static class Tab extends Button {
        private final BundledTabs bundle;

        protected Tab(int x, int y, BundledTabs bundle, Button.OnPress onPress) {
            super(x, y, 16, 16, Component.empty(), onPress, DEFAULT_NARRATION);
            this.bundle = bundle;
            bundle.setContentTab(this);
            this.setTooltip(Tooltip.create(bundle.getTooltip()));
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 20.0F);
            this.renderSelected(graphics);
            graphics.renderItem(this.bundle.getIcon(), this.getX(), this.getY());
            graphics.pose().popPose();
            this.renderHighlight(graphics);
        }

        private void renderSelected(GuiGraphics graphics) {
            if (this.bundle.isSelected()) {
                graphics.blit(BundledTabSelector.SELECTOR_BAR, this.getX() - 7, this.getY() - 1, 36, 24, 30, 19);
            }
        }

        private void renderHighlight(GuiGraphics graphics) {
            if (this.isHovered() && !this.bundle.isSelected()) {
                graphics.pose().pushPose();
                graphics.pose().translate(0.0F, 0.0F, 20.0F);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                graphics.blit(BundledTabSelector.SELECTOR_BAR, this.getX(), this.getY(), 32, 44, 16, 16);
                RenderSystem.disableBlend();
                graphics.pose().popPose();
            }
        }
    }

    public static class ScrollButton extends Button {
        private final int uOffset;

        public ScrollButton(int x, int y, int uOffset, Button.OnPress onPress) {
            super(x, y, 18, 20, Component.empty(), onPress, DEFAULT_NARRATION);
            this.uOffset = uOffset;
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            int textureY = this.isHovered() ? 12 : 0;
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 20.0F);
            graphics.blit(BundledTabSelector.SELECTOR_BAR, this.getX(), this.getY(), this.uOffset, textureY, 18, 11);
            graphics.pose().popPose();
        }
    }
}
