package com.otterly76.ott;

import com.otterly76.ott.client.DebugInfoHandler;
import com.otterly76.ott.client.HUDOverlayHandler;
import com.otterly76.ott.client.TooltipOverlayHandler;
import com.otterly76.ott.network.SyncHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class HungerClass {
    public HungerClass(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::onRegisterPayloadHandler);
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(this::preInitClient);
            modEventBus.addListener(this::onRegisterHudHandler);
            modEventBus.addListener(this::onRegisterClientTooltipComponentFactories);
        }
    }

    private void preInitClient(FMLClientSetupEvent event) {
        DebugInfoHandler.init();
        TooltipOverlayHandler.init();
    }

    private void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        TooltipOverlayHandler.register(event);
    }

    @SubscribeEvent
    private void onRegisterPayloadHandler(RegisterPayloadHandlersEvent event) {
        SyncHandler.register(event);
    }

    @SubscribeEvent
    private void onRegisterHudHandler(RegisterGuiLayersEvent event) {
        HUDOverlayHandler.register(event);
    }
}