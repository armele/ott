package com.otterly76.ott.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SplashParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashParticle.class)
public abstract class WaterDropParticleMixin extends TextureSheetParticleMixin {
    protected WaterDropParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
        super(clientLevel, d, e, f);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    public void onConstruct(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, CallbackInfo ci) {
    }
}