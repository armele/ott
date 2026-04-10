package com.otterly76.ott.mixin.client;

import com.otterly76.ott.Constants;
import com.otterly76.ott.network.recycling.ServerboundOpenRecyclingPacket;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(CraftingScreen.class)
public abstract class CraftingScreenMixin extends AbstractContainerScreen<CraftingMenu> {
    @Unique
    private static final WidgetSprites RECYCLE_BUTTON_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycle_off"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycle_on")
    );

    public CraftingScreenMixin(CraftingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void onInit(CallbackInfo ci) {
        // Fully remove the recipe book toggle button (at leftPos + 5) from all widget tracking lists
        new ArrayList<>(this.renderables).stream()
                .filter(w -> w instanceof ImageButton b && b.getX() == this.leftPos + 5)
                .forEach(w -> removeWidget((GuiEventListener) w));

        // Place recycle button where the recipe book toggle was
        this.addRenderableWidget(new ImageButton(this.leftPos + 8, this.topPos + 35, 15, 15, RECYCLE_BUTTON_SPRITES, (button) -> {
            PacketDistributor.sendToServer(new ServerboundOpenRecyclingPacket());
        }));
    }

    // Block the direct recipeBookComponent.mouseClicked() call so the recipe book cannot be opened
    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;mouseClicked(DDI)Z"))
    private boolean ott$suppressRecipeBookClick(RecipeBookComponent component, double mouseX, double mouseY, int button) {
        return false;
    }
}