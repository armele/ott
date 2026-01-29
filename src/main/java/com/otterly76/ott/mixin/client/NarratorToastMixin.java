package com.otterly76.ott.mixin.client;


import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SystemToast.class)
public class NarratorToastMixin {
    @Inject(method = "addOrUpdate", at = @At("HEAD"), cancellable = true)
    private static void ott$silenceNarratorToast(ToastComponent toastComponent, SystemToast.SystemToastId id, Component title, @Nullable Component message, CallbackInfo ci) {
        // NARRATOR_TOGGLE is the specific ID used when the narrator shouts at you on first load
        if (id == SystemToast.SystemToastId.NARRATOR_TOGGLE) {
            ci.cancel();
        }
    }
}
