package com.otterly76.ott.mixin.common;

import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PrimaryLevelData.class, remap = false)
public class PrimaryLevelDataMixin {
    @Inject(method = "hasConfirmedExperimentalWarning", at = @At("HEAD"), cancellable = true)
    public void ott$hasConfirmedExperimentalWarning(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
