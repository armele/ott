package com.otterly76.ott.mixin.client;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.network.ServerboundOpenItemPacket;
import com.otterly76.ott.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class ContainerClickMixin {
    @Shadow public abstract Slot getSlotUnderMouse();
    @Final
    @Shadow protected net.minecraft.world.inventory.AbstractContainerMenu menu;

    @Unique
    private static boolean ott$shouldReturnToInventory = false;

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ott$onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!OttConfig.GENERAL.ENABLE_RIGHT_CLICK_OPEN.get()) return;
        if (button == 2) { // Middle click
            Slot slot = getSlotUnderMouse();
            if (slot != null && slot.hasItem() && slot.getItem().is(ModTags.ItemTags.INVENTORY_OPENABLE)) {
                if (slot.container instanceof Inventory) {
                    int slotIndex = slot.getContainerSlot();

                    Minecraft mc = Minecraft.getInstance();
                    if (mc.getConnection() != null) {
                        mc.getConnection().send(new ServerboundOpenItemPacket(slotIndex));
                        ott$shouldReturnToInventory = true;
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }

    @Inject(method = "removed", at = @At("RETURN"))
    private void ott$onRemoved(CallbackInfo ci) {
        if (ott$shouldReturnToInventory && !(this.menu instanceof net.minecraft.world.inventory.InventoryMenu)) {
            ott$shouldReturnToInventory = false;
            Minecraft mc = Minecraft.getInstance();
            mc.tell(() -> {
                if (mc.player != null) {
                    mc.setScreen(new InventoryScreen(mc.player));
                }
            });
        }
    }
}
