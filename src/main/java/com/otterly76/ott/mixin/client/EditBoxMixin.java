package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditBox.class)
public abstract class EditBoxMixin {
    @Shadow
    private int maxLength;

    @Inject(method = "setValue", at = @At("HEAD"))
    private void ott$onSetValue(String value, CallbackInfo ci) {
        if (value != null && value.contains("&")) {
            this.maxLength = 1024;
        }
    }
}
