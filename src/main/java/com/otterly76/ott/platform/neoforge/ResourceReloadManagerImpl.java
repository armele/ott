package com.otterly76.ott.platform.neoforge;

import com.otterly76.ott.platform.core.events.ResourceReloadManager;
import com.otterly76.ott.platform.neoforge.client.ForgeClientEventHandler;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.function.Consumer;

public class ResourceReloadManagerImpl {
    public static void registerClient(Consumer<ResourceReloadManager.ListenerEvent> exporter) {
        ForgeClientEventHandler.registerClientResourceListeners(exporter);
    }

    public static void registerServer(Consumer<ResourceReloadManager.ListenerEvent> exporter) {
        Consumer<AddReloadListenerEvent> consumer = (event) -> {
            ResourceReloadManager.ListenerEvent listener = (id, reloadListener) -> event.addListener(reloadListener);
            exporter.accept(listener);
        };
        NeoForge.EVENT_BUS.addListener(consumer);
    }
}
