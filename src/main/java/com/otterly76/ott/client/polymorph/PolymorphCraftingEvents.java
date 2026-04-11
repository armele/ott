package com.otterly76.ott.client.polymorph;

import com.otterly76.ott.Constants;
import com.otterly76.ott.network.polymorph.ClientboundCraftingRecipesPacket;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class PolymorphCraftingEvents {

    @Nullable
    private static PolymorphCraftingWidget current = null;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof CraftingScreen cs) {
            // Output slot is always index 0 in CraftingMenu
            current = new PolymorphCraftingWidget(cs, cs.getMenu().slots.getFirst());
        } else {
            current = null;
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (current != null && event.getScreen() instanceof CraftingScreen) {
            current.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
        }
    }

    @SubscribeEvent
    public static void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        if (current != null && event.getScreen() instanceof CraftingScreen) {
            if (current.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton())) {
                event.setCanceled(true);
            }
        }
    }

    public static void setRecipesList(ClientboundCraftingRecipesPacket packet) {
        if (current != null) {
            current.setRecipesList(packet.recipes(), packet.selected().orElse(null));
        }
    }
}
