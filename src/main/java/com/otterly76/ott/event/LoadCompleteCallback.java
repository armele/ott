package com.otterly76.ott.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@FunctionalInterface
public interface LoadCompleteCallback {
    List<LoadCompleteCallback> CALLBACKS = new CopyOnWriteArrayList<>();

    void onLoadComplete();

    static void register(LoadCompleteCallback callback) {
        CALLBACKS.add(callback);
    }

    static void fire() {
        CALLBACKS.forEach(LoadCompleteCallback::onLoadComplete);
    }
}