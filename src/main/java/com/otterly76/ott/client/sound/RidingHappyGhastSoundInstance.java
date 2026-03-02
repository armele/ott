package com.otterly76.ott.client.sound;

import com.otterly76.ott.entity.custom.HappyGhast;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RidingHappyGhastSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;
    private final HappyGhast happyGhast;

    public RidingHappyGhastSoundInstance(Player player, HappyGhast happyGhast) {
        super(ModSounds.HAPPY_GHAST_RIDING.get(), happyGhast.getSoundSource(), SoundInstance.createUnseededRandom());
        this.player = player;
        this.happyGhast = happyGhast;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.happyGhast.isRemoved() && this.player.isPassenger() && this.player.getVehicle() == this.happyGhast) {
            float speed = (float)this.happyGhast.getDeltaMovement().length();
            if (speed >= 0.01F) {
                this.volume = 5.0F * Mth.clampedLerp(0.0F, 1.0F, speed);
            } else {
                this.volume = 0.0F;
            }
        } else {
            this.stop();
        }
    }
}
