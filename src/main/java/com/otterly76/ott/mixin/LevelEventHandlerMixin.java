package com.otterly76.ott.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LevelRenderer.class)
public abstract class LevelEventHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private ClientLevel level;
    @Shadow
    @Final
    private Map<BlockPos, SoundInstance> playingJukeboxSongs;

    @Shadow
    protected abstract void stopJukeboxSong(BlockPos pos);

    @Shadow
    protected abstract void notifyNearbyEntities(Level level, BlockPos pos, boolean playStatus);

    @Inject(
            at = @At("HEAD"),
            method = "playJukeboxSong",
            cancellable = true
    )
    private void playJukeboxSong(Holder<JukeboxSong> songHolder, BlockPos pos, CallbackInfo ci) {
        if (this.level != null) {
            this.stopJukeboxSong(pos);
            JukeboxSong jukeboxSong = songHolder.value();
            SoundEvent soundEvent = jukeboxSong.soundEvent().value();

            // Create a looping sound instance
            SoundInstance soundInstance = ott$createLoopingSound(soundEvent, Vec3.atCenterOf(pos));

            this.playingJukeboxSongs.put(pos, soundInstance);
            this.minecraft.getSoundManager().play(soundInstance);
            this.minecraft.gui.setNowPlaying(jukeboxSong.description());
            this.notifyNearbyEntities(this.level, pos, true);
            ci.cancel();
        }
    }

    @Unique
    private static SimpleSoundInstance ott$createLoopingSound(SoundEvent soundEvent, Vec3 pos) {
        return new SimpleSoundInstance(
                soundEvent.getLocation(),
                SoundSource.RECORDS,
                4.0F, // volume
                1.0F, // pitch
                SoundInstance.createUnseededRandom(),
                true, // looping <--- This is the key
                0,    // delay
                SoundInstance.Attenuation.LINEAR,
                pos.x,
                pos.y,
                pos.z,
                false // relative
        );
    }
}