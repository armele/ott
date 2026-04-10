package com.otterly76.ott.mixin.client;

import com.otterly76.ott.Constants;
import com.otterly76.ott.network.ServerboundOpenTrashPacket;
import com.otterly76.ott.network.recycling.ServerboundOpenRecyclingPacket;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {
    @Shadow private boolean widthTooNarrow;
    @Shadow @Final
    private RecipeBookComponent recipeBookComponent;

    @Unique
    private static final WidgetSprites TRASH_BUTTON_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "trash_off"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "trash_on")
    );

    @Unique
    private static final WidgetSprites RECYCLE_BUTTON_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycle_off"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recycle_on")
    );

    public InventoryScreenMixin(InventoryMenu menu, net.minecraft.world.entity.player.Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void onInit(CallbackInfo ci) {
        // Fully remove the recipe book toggle button from renderables, children, AND narratables
        // so it is neither visible nor a click target
        new ArrayList<>(this.renderables).stream()
                .filter(w -> w instanceof ImageButton b && b.getX() == this.leftPos + 104)
                .forEach(w -> removeWidget((GuiEventListener) w));

        int x = this.leftPos + 153;
        int y = this.topPos + 64;

        this.addRenderableWidget(new ImageButton(x - 16, y, 15, 15, RECYCLE_BUTTON_SPRITES, (button) -> {
            PacketDistributor.sendToServer(new ServerboundOpenRecyclingPacket());
        }));

        this.addRenderableWidget(new ImageButton(x, y, 15, 15, TRASH_BUTTON_SPRITES, (button) -> {
            PacketDistributor.sendToServer(new ServerboundOpenTrashPacket());
        }));
    }

    // Block the direct recipeBookComponent.mouseClicked() call in InventoryScreen.mouseClicked
    // so the recipe book cannot be opened even though its component is still present
    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;mouseClicked(DDI)Z"))
    private boolean ott$suppressRecipeBookClick(RecipeBookComponent component, double mouseX, double mouseY, int button) {
        return false;
    }
}