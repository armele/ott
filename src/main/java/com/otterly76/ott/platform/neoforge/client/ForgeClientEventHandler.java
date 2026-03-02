package com.otterly76.ott.platform.neoforge.client;

import com.otterly76.ott.platform.core.events.ResourceReloadManager;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.util.Objects;
import java.util.function.Consumer;

public class ForgeClientEventHandler {
    public static void registerClientResourceListeners(Consumer<ResourceReloadManager.ListenerEvent> exporter) {
        Consumer<RegisterClientReloadListenersEvent> consumer = (event) -> {
            ResourceReloadManager.ListenerEvent listener = (id, reloadListener) -> event.registerReloadListener(reloadListener);
            exporter.accept(listener);
        };
        var container = ModLoadingContext.get().getActiveContainer();
        if (container != null) {
            Objects.requireNonNull(container.getEventBus()).addListener(consumer);
        }
    }
}
