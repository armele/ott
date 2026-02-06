package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin {

    @Shadow
    private EditBox name;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void ott$onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            if (this.name.isFocused()) {
                AnvilScreen screen = (AnvilScreen) (Object) this;
                AnvilMenu menu = screen.getMenu();
                Slot resultSlot = menu.getSlot(2);
                if (resultSlot.hasItem()) {
                    // We can't easily check mayPickup on client side as it might be protected or have different logic,
                    // but we can try to click it. If the server rejects it, nothing happens.
                    if (screen.getMinecraft().gameMode != null && screen.getMinecraft().player != null) {
                        screen.getMinecraft().gameMode.handleInventoryMouseClick(menu.containerId, 2, 0, ClickType.QUICK_MOVE, screen.getMinecraft().player);
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}
