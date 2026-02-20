package com.otterly76.ott.mixin.client;

import com.otterly76.ott.client.music.MusicFadeManager;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Shadow
    private @Nullable SoundInstance currentMusic;
    @Unique
    private MusicFadeManager ott$fadeManager;

    @Unique
    private MusicFadeManager ott$getFadeManager() {
        if (this.ott$fadeManager == null) {
            this.ott$fadeManager = new MusicFadeManager((MusicManager) (Object) this);
        }

        return this.ott$fadeManager;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onTick(CallbackInfo ci) {
        if (this.ott$getFadeManager().onTick(this.currentMusic)) {
            ci.cancel();
        }

    }

    @Inject(
            method = "startPlaying",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventPlayingInPaleGarden(Music selector, CallbackInfo ci) {
        if (this.ott$getFadeManager().preventPlayingInPaleGarden()) {
            ci.cancel();
        }

    }

    @Inject(
            method = "startPlaying",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"
            )
    )
    private void updateVolume(Music selector, CallbackInfo ci) {
        this.ott$getFadeManager().updateVolume(this.currentMusic);
    }

    @Inject(
            method = "startPlaying",
            at = @At("TAIL")
    )
    private void onStartPlaying(Music selector, CallbackInfo ci) {
        this.ott$getFadeManager().onStartPlaying();
    }
}