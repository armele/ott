package com.otterly76.ott.client;

import com.otterly76.ott.ClientModEvents;
import com.otterly76.ott.event.ModEventBusEvents;
import net.neoforged.bus.api.IEventBus;

public class OttClient {
    public static void init(IEventBus modEventBus) {
        ClientModEvents.register(modEventBus);
        modEventBus.addListener(ModEventBusEvents::registerLayers);
    }
}