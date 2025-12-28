package com.otterly76.ott.mixin;

import com.otterly76.ott.Constants;
import com.otterly76.ott.network.ServerboundOpenTrashPacket;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {
    @Unique
    private static final WidgetSprites TRASH_BUTTON_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "trash_off"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "trash_on")
    );

    public InventoryScreenMixin(InventoryMenu menu, net.minecraft.world.entity.player.Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void onInit(CallbackInfo ci) {
        // Positioned at the top right of the inventory grid
        int x = this.leftPos + 151;
        int y = this.topPos + 63;

        this.addRenderableWidget(new ImageButton(x, y, 20, 18, TRASH_BUTTON_SPRITES, (button) -> {
            PacketDistributor.sendToServer(new ServerboundOpenTrashPacket());
        }));
    }
}