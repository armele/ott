package com.otterly76.ott.mixin.client;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void ott$silenceEarlySounds(SoundInstance sound, CallbackInfo ci) {
        // If we are in the middle of the loading screen,
        // some mod configs (like MineColonies) might not be ready.
        // We cancel the sound play to prevent their event listeners from crashing.
        try {
            // We just need a way to check if 'boot' is done.
            // A simple way is to check if a known config is reachable.
            // If this throws, we skip playing the sound.
            com.otterly76.ott.config.OttConfig.WEATHER.BIOME_TINT.get();
        } catch (IllegalStateException e) {
            ci.cancel();
        }
    }
}