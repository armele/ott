package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.inventory.EngravingTableMenu;
import com.otterly76.ott.inventory.RecyclingMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(AbstractContainerScreen.class)
public abstract class InventorySearchMixin extends Screen {
    @Shadow protected int leftPos;
    @Shadow protected int topPos;
    @Shadow protected int imageWidth;
    @Shadow protected int imageHeight;
    @Final
    @Shadow protected net.minecraft.world.inventory.AbstractContainerMenu menu;

    @Unique
    private static final ResourceLocation ott$SEARCH_SPRITE = ResourceLocation.withDefaultNamespace("icon/search");
    @Unique
    private static final Component ott$SEARCH_HINT = Component.translatable("gui.recipebook.search_hint")
            .withStyle(ChatFormatting.ITALIC)
            .withStyle(ChatFormatting.GRAY);

    @Unique
    private EditBox ott$searchBox;

    protected InventorySearchMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void ott$onInit(CallbackInfo ci) {
        if (OttConfig.GENERAL.ENABLE_INVENTORY_SEARCH.get()
                && !(this.menu instanceof CreativeModeInventoryScreen.ItemPickerMenu)
                && !(this.menu instanceof RecyclingMenu)
                && !(this.menu instanceof EngravingTableMenu)) {
            int boxWidth = 80;
            int x = this.leftPos + this.imageWidth - boxWidth - 6;
            int y = this.topPos + 6;
            this.ott$searchBox = new EditBox(this.font, x, y, boxWidth, 10, Component.translatable("itemGroup.search"));
            this.ott$searchBox.setHint(ott$SEARCH_HINT);
            this.ott$searchBox.setBordered(false);
            this.ott$searchBox.setVisible(false);
            this.ott$searchBox.setTextColor(0xFFFFFF);
            this.addRenderableWidget(this.ott$searchBox);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBackground(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.AFTER))
    private void ott$onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.ott$searchBox == null || !OttConfig.GENERAL.ENABLE_INVENTORY_SEARCH.get()) return;

        int iconX = this.leftPos + this.imageWidth - 14;
        int iconY = this.topPos + 6;

        if (this.ott$searchBox.isVisible()) {
            // Background for search box
            int bgX = this.ott$searchBox.getX() - 2;
            int bgY = this.ott$searchBox.getY() - 2;
            guiGraphics.fill(bgX, bgY, bgX + this.ott$searchBox.getWidth() + 4, bgY + 12, 0xA0000000);
        }

        guiGraphics.blitSprite(ott$SEARCH_SPRITE, iconX, iconY, 8, 8);
    }

    @Inject(method = "renderSlot", at = @At("RETURN"))
    private void ott$afterRenderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (this.ott$searchBox != null && this.ott$searchBox.isVisible() && !this.ott$searchBox.getValue().isEmpty()) {
            if (!ott$itemMatchesSearch(slot.getItem())) {
                RenderSystem.disableDepthTest();
                guiGraphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 0x80000000);
                RenderSystem.enableDepthTest();
            }
        }
    }

    @Unique
    private boolean ott$itemMatchesSearch(ItemStack stack) {
        if (stack.isEmpty()) return false;
        String search = this.ott$searchBox.getValue().toLowerCase(Locale.ROOT).trim();
        if (search.isEmpty()) return true;

        if (stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains(search)) return true;

        return stack.getTags().anyMatch(tag -> tag.location().toString().toLowerCase(Locale.ROOT).contains(search));
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ott$onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.ott$searchBox == null || !OttConfig.GENERAL.ENABLE_INVENTORY_SEARCH.get()) return;

        int iconX = this.leftPos + this.imageWidth - 18;
        int iconY = this.topPos + 5;

        if (mouseX >= iconX && mouseX <= iconX + 12 && mouseY >= iconY && mouseY <= iconY + 12) {
            this.ott$searchBox.setVisible(!this.ott$searchBox.isVisible());
            if (this.ott$searchBox.isVisible()) {
                this.ott$searchBox.setFocused(true);
                this.setFocused(this.ott$searchBox);
            } else {
                this.ott$searchBox.setValue("");
                this.ott$searchBox.setFocused(false);
            }
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "removed", at = @At("RETURN"))
    private void ott$onRemoved(CallbackInfo ci) {
        if (this.ott$searchBox != null) {
            this.ott$searchBox.setValue("");
            this.ott$searchBox.setVisible(false);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void ott$onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.ott$searchBox != null && this.ott$searchBox.isVisible() && this.ott$searchBox.isFocused()) {
            if (keyCode == 256) { // ESC
                this.ott$searchBox.setFocused(false);
                this.ott$searchBox.setVisible(false);
                this.ott$searchBox.setValue("");
                cir.setReturnValue(true);
                return;
            }
            if (this.ott$searchBox.keyPressed(keyCode, scanCode, modifiers)) {
                cir.setReturnValue(true);
                return;
            }
            // Prevent container from closing when typing in search box
            if (this.minecraft != null && this.minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
                cir.setReturnValue(true);
            }
        }
    }
}