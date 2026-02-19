package com.otterly76.ott.mixin.client;

import com.otterly76.ott.client.handler.BundleMouseActions;
import com.otterly76.ott.util.item.BundleFeatures;
import com.otterly76.ott.util.ModTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {
    @Shadow
    protected int imageWidth;
    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Unique
    private Slot vb$lastHoveredSlot;

    @Unique
    private static final ResourceLocation BUNDLE_OPEN_BACK = ResourceLocation.withDefaultNamespace("textures/item/bundle_open_back.png");
    @Unique
    private static final ResourceLocation BUNDLE_OPEN_FRONT = ResourceLocation.withDefaultNamespace("textures/item/bundle_open_front.png");

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void vb$checkStopHovering(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.hoveredSlot != this.vb$lastHoveredSlot) {
            if (this.vb$lastHoveredSlot != null && this.vb$lastHoveredSlot.hasItem() && BundleMouseActions.INSTANCE.matches(this.vb$lastHoveredSlot)) {
                if (BundleFeatures.onBundleUpdate()) {
                    BundleMouseActions.INSTANCE.onStopHovering(this.vb$lastHoveredSlot);
                }
            }
            this.vb$lastHoveredSlot = this.hoveredSlot;
        }
    }

    @Inject(
            method = "slotClicked",
            at = @At("HEAD")
    )
    private void vb$onSlotClicked(Slot slot, int slotId, int mouseButton, ClickType clickType, CallbackInfo ci) {
        if (slot != null && slot.hasItem() && BundleMouseActions.INSTANCE.matches(slot)) {
            if (BundleFeatures.onBundleUpdate()) {
                BundleMouseActions.INSTANCE.onSlotClicked(slot, clickType);
            }
        }
    }

    @Inject(
            method = "renderSlot",
            at = @At("HEAD"),
            cancellable = true
    )
    private void vb$renderBundleContents(GuiGraphics graphics, Slot slot, CallbackInfo ci) {
        ItemStack stack = slot.getItem();
        if (stack.is(ModTags.ItemTags.BUNDLES)) {
            ItemStack selectedItem = BundleFeatures.getSelectedItemStack(stack);
            if (!selectedItem.isEmpty()) {
                ResourceLocation backTexture = BUNDLE_OPEN_BACK;
                ResourceLocation frontTexture = BUNDLE_OPEN_FRONT;

                for(DyeColor color : DyeColor.values()) {
                    if (stack.is(BundleFeatures.getByColor(color)) && !stack.is(Items.BUNDLE)) {
                        backTexture = ResourceLocation.withDefaultNamespace("textures/item/" + color.getName() + "_bundle_open_back.png");
                        frontTexture = ResourceLocation.withDefaultNamespace("textures/item/" + color.getName() + "_bundle_open_front.png");
                        break;
                    }
                }

                PoseStack pose = graphics.pose();
                int slotX = slot.x;
                int slotY = slot.y;
                pose.pushPose();
                pose.translate(0.0F, 0.0F, 100.0F);
                graphics.blit(backTexture, slotX, slotY, 0.0F, 0.0F, 16, 16, 16, 16);
                graphics.renderItem(selectedItem, slotX, slotY, slot.x + slot.y * this.imageWidth);
                pose.pushPose();
                pose.translate(0.0F, 0.0F, 200.0F);
                graphics.blit(frontTexture, slotX, slotY, 0.0F, 0.0F, 16, 16, 16, 16);
                graphics.renderItemDecorations(this.font, stack, slotX, slotY);
                pose.popPose();
                pose.popPose();
                ci.cancel();
            }
        }
    }
}
