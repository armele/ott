package com.otterly76.ott.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.function.Consumer;

@Mixin(Minecraft.class)
public abstract class MinecraftSkipExperimentalWarningMixin { //TODO not working, troubleshoot

    @Inject(method = "setScreen", at = @At("TAIL"))
    private void ott$skipExperimentalWarning(Screen screen, CallbackInfo ci) {
        if (screen == null) return;

        String name = screen.getClass().getName();
        if (!name.contains("Experimental") && !name.toLowerCase().contains("experiment")) return;

        Minecraft.getInstance().execute(() -> ott$tryProceed(screen));
    }

    @Unique
    private static void ott$tryProceed(Screen screen) {
        if (ott$invokeNoArgIfExists(screen, "proceed")) return;
        if (ott$invokeNoArgIfExists(screen, "accept")) return;
        if (ott$invokeNoArgIfExists(screen, "onProceed")) return;

        for (Field f : screen.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                Object v = f.get(screen);

                if (v instanceof Runnable r) {
                    r.run();
                    return;
                }
                if (v instanceof Consumer<?> c) {
                    @SuppressWarnings("unchecked")
                    Consumer<Boolean> cb = (Consumer<Boolean>) c;
                    cb.accept(true);
                    return;
                }
            } catch (Throwable ignored) {
            }
        }

        screen.onClose();
    }

    @Unique
    private static boolean ott$invokeNoArgIfExists(Object target, String methodName) {
        try {
            var m = target.getClass().getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(target);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}