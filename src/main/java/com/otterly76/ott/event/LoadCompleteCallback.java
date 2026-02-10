package com.otterly76.ott.event;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface LoadCompleteCallback {
    List<LoadCompleteCallback> CALLBACKS = new ArrayList<>();

    void onLoadComplete();

    static void register(LoadCompleteCallback callback) {
        CALLBACKS.add(callback);
    }

    static void fire() {
        CALLBACKS.forEach(LoadCompleteCallback::onLoadComplete);
    }
}