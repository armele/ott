package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditBox.class)
public abstract class EditBoxMixin {
    @Shadow private int maxLength;

    @Inject(method = "setValue", at = @At("HEAD"))
    private void ott$increaseMaxLength(String value, CallbackInfo ci) {
        // If the value contains '&', it might be a formatting code.
        // We temporarily increase maxLength to allow the translated '§' codes if they were somehow longer,
        // but more importantly, we want to allow the user to type more if they use codes.
        // Actually, the main issue is just allowing '§' to be rendered or '&' to be typed.
        if (value != null && value.contains("&")) {
            if (this.maxLength < 50) { // arbitrary increase for renaming
                this.maxLength = 100;
            }
        }
    }
}