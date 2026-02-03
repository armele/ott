package com.otterly76.ott.mixin.common;

import net.minecraft.core.Holder;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(JukeboxSongPlayer.class)
public abstract class JukeboxSongPlayerMixin {
    @Shadow
    private long ticksSinceSongStarted;
    @Shadow
    private Holder<JukeboxSong> song;

    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(
            at = @At("HEAD"),
            method = "tick",
            cancellable = true
    )
    public void ott$loopTick(LevelAccessor level, @Nullable BlockState state, CallbackInfo ci) {
        if (this.song != null && this.song.value().hasFinished(this.ticksSinceSongStarted)) {
            // Instead of stopping, we reset the timer to loop the logic
            this.ticksSinceSongStarted = 0L;
            // We don't necessarily need to cancel if we want the jukebox to continue ticking,
            // but resetting the ticks effectively loops the song duration check.
        }
    }
}