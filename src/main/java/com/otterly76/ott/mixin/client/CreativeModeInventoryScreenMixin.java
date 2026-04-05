package com.otterly76.ott.mixin.client;

import com.otterly76.ott.ModCreativeTabs;
import com.otterly76.ott.client.gui.creative.OttCategoryButton;
import com.otterly76.ott.client.gui.creative.OttCreativeCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin
        extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Shadow private static CreativeModeTab selectedTab;
    @Shadow protected abstract void selectTab(CreativeModeTab tab);

    @Unique private static final int BTN_STEP = 28; // 26px button + 2px gap

    @Unique private final List<OttCategoryButton> ott$categoryButtons = new ArrayList<>();
    @Unique private int ott$categoryScroll = 0;

    protected CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu,
                                               Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void ott$onSelectTab(CreativeModeTab tab, CallbackInfo ci) {
        // Remove any existing category buttons
        for (OttCategoryButton btn : ott$categoryButtons) {
            this.removeWidget(btn);
        }
        ott$categoryButtons.clear();

        if (tab != ModCreativeTabs.OTT_MAIN.get()) return;

        List<OttCreativeCategories> order = OttCreativeCategories.DISPLAY_ORDER;
        int total = order.size();

        // How many buttons fit in the panel (imageHeight inherited from AbstractContainerScreen)
        int panelHeight = this.imageHeight - 16; // 8px margins top + bottom
        int visible = Math.max(1, panelHeight / BTN_STEP);

        // Clamp scroll so we never show an empty gap at the bottom
        ott$categoryScroll = Math.clamp(ott$categoryScroll, 0, total - visible);

        int bx = this.leftPos - 26 - 4;
        int by = this.topPos + 8;

        int end = Math.min(total, ott$categoryScroll + visible);
        for (int i = ott$categoryScroll; i < end; i++) {
            final OttCreativeCategories cat = order.get(i);
            int btnY = by + (i - ott$categoryScroll) * BTN_STEP;
            OttCategoryButton btn = new OttCategoryButton(bx, btnY, cat, b -> {
                OttCreativeCategories.setSelected(OttCreativeCategories.getSelected() == cat ? null : cat);
                this.selectTab(ModCreativeTabs.OTT_MAIN.get());
            });
            this.addRenderableWidget(btn);
            ott$categoryButtons.add(btn);
        }

        // Filter menu.items to the selected category
        OttCreativeCategories selected = OttCreativeCategories.getSelected();
        if (selected != null) {
            Set<Item> categoryItems = new HashSet<>();
            // populateItems accepts null params — none of the current populators use them
            selected.populateItems(null, (stack, visibility) -> categoryItems.add(stack.getItem()));
            this.menu.items
                    .removeIf(stack -> !categoryItems.contains(stack.getItem()));
        }

        // Re-sync the visible item slots with the (filtered) menu.items list.
        // selectTab already called scrollTo(0) before our inject ran, but at that point
        // menu.items still held the full unfiltered set — call again now that it's filtered.
        this.menu.scrollTo(0.0F);
    }

    // Scroll the category button panel with the mouse wheel when hovering over it
    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void ott$onMouseScrolled(double mouseX, double mouseY,
                                     double scrollX, double scrollY,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (selectedTab != ModCreativeTabs.OTT_MAIN.get()) return;

        List<OttCreativeCategories> order = OttCreativeCategories.DISPLAY_ORDER;
        int total = order.size();
        int panelHeight = this.imageHeight - 16;
        int visible = Math.max(1, panelHeight / BTN_STEP);

        if (total <= visible) return; // nothing to scroll

        int bx = this.leftPos - 26 - 4;
        int by = this.topPos + 8;
        int bxEnd = bx + 26;
        int byEnd = by + visible * BTN_STEP;

        if (mouseX >= bx && mouseX < bxEnd && mouseY >= by && mouseY < byEnd) {
            int newScroll = ott$categoryScroll - (int) Math.signum(scrollY);
            newScroll = Math.clamp(newScroll, 0, total - visible);
            if (newScroll != ott$categoryScroll) {
                ott$categoryScroll = newScroll;
                this.selectTab(ModCreativeTabs.OTT_MAIN.get());
            }
            cir.setReturnValue(true);
        }
    }

    // Draw ▲ / ▼ scroll hint arrows when the category panel has hidden entries
    @Inject(method = "render", at = @At("TAIL"))
    private void ott$renderScrollArrows(@SuppressWarnings("unused") GuiGraphics graphics,
                                        @SuppressWarnings("unused") int mouseX,
                                        @SuppressWarnings("unused") int mouseY,
                                        @SuppressWarnings("unused") float partialTick,
                                        CallbackInfo ci) {
        if (selectedTab != ModCreativeTabs.OTT_MAIN.get()) return;

        List<OttCreativeCategories> order = OttCreativeCategories.DISPLAY_ORDER;
        int total = order.size();
        int panelHeight = this.imageHeight - 16;
        int visible = Math.max(1, panelHeight / BTN_STEP);
        if (total <= visible) return;

        int bx = this.leftPos - 26 - 4;
        int by = this.topPos + 8;
        int cx = bx + 13; // horizontal centre of the button column

        if (ott$categoryScroll > 0) {
            // Arrow above first visible button
            graphics.drawCenteredString(this.font, "▲", cx, by - 9, 0xFFBBBBBB);
        }
        if (ott$categoryScroll + visible < total) {
            // Arrow below last visible button
            graphics.drawCenteredString(this.font, "▼", cx, by + visible * BTN_STEP + 2, 0xFFBBBBBB);
        }
    }
}
