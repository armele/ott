package com.otterly76.ott.platform.core.events;

import com.otterly76.ott.platform.neoforge.ResourceReloadManagerImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public class ResourceReloadManager {
    public static void registerClient(Consumer<ListenerEvent> listener) {
        ResourceReloadManagerImpl.registerClient(listener);
    }

    public static void registerServer(Consumer<ListenerEvent> listener) {
        ResourceReloadManagerImpl.registerServer(listener);
    }

    public interface ListenerEvent {
        void register(ResourceLocation id, PreparableReloadListener reloadListener);
    }
}
