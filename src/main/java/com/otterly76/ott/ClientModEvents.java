package com.otterly76.ott;

import com.otterly76.ott.client.NutritionHudOverlay;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public class ClientModEvents {

    public static void register(IEventBus modBus) {
        modBus.addListener(ClientModEvents::registerGuiLayers);
    }

    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "nutrition_overlay"),
                new NutritionHudOverlay());
    }
}