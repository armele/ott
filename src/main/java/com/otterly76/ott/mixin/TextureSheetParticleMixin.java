package com.otterly76.ott.mixin;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureSheetParticle.class)
public abstract class TextureSheetParticleMixin extends SingleQuadParticle {
    protected TextureSheetParticleMixin(net.minecraft.client.multiplayer.ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(method = "pickSprite", at = @At("HEAD"))
    public void pickSprite(SpriteSet spriteSet, CallbackInfo ci) {
    }
}