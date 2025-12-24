package com.otterly76.ott.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DripParticle.class)
public abstract class DripParticleMixin extends TextureSheetParticle {

    protected DripParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    // Using the <init> wildcard to target the constructor reliably
    @Inject(method = "<init>", at = @At("RETURN"))
    private void ott$removeWaterTint(ClientLevel level, double x, double y, double z, Fluid fluid, CallbackInfo ci) {
        // Compare Fluid instances directly using ==
        if (fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER) {
            this.setColor(1.0F, 1.0F, 1.0F);
        }
    }
}