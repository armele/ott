package com.otterly76.ott.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DripParticle.class)
public abstract class DripParticleMixin extends TextureSheetParticle {

    @Shadow @Final
    private Fluid type;

    protected DripParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    // Injecting into tick ensures that even if something else tries to re-tint it,
    // we force it back to white every frame.
    @Inject(method = "tick", at = @At("HEAD"))
    private void ott$forceNoTint(CallbackInfo ci) {
        if (this.type == Fluids.WATER || this.type == Fluids.FLOWING_WATER) {
            this.rCol = 1.0F;
            this.gCol = 1.0F;
            this.bCol = 1.0F;
        }
    }
}