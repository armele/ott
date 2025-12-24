package com.otterly76.ott.mixin;

import net.minecraft.client.particle.WaterDropParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterDropParticle.class)
public class WaterDropParticleMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
    }
}