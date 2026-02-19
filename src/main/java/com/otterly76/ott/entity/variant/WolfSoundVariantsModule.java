package com.otterly76.ott.entity.variant;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Wolf;

public class WolfSoundVariantsModule {
    public static <T extends Wolf & WolfSoundVariantHolder> SoundEvent getAmbientSound(T wolf) {
        if (!OttConfig.GENERAL.HAS_WOLF_SOUND_VARIANTS.get()) {
            return null;
        } else if (wolf.isAngry()) {
            return wolf.getSoundVariant().growlSound().value();
        } else if (wolf.getRandom().nextInt(3) != 0) {
            return wolf.getSoundVariant().ambientSound().value();
        } else {
            return wolf.isTame() && wolf.getHealth() < 20.0F ? wolf.getSoundVariant().whineSound().value() : wolf.getSoundVariant().pantSound().value();
        }
    }

    public static <T extends Wolf & WolfSoundVariantHolder> SoundEvent getHurtSound(T wolf) {
        return !OttConfig.GENERAL.HAS_WOLF_SOUND_VARIANTS.get() ? null : wolf.getSoundVariant().hurtSound().value();
    }

    public static <T extends Wolf & WolfSoundVariantHolder> SoundEvent getDeathSound(T wolf) {
        return !OttConfig.GENERAL.HAS_WOLF_SOUND_VARIANTS.get() ? null : wolf.getSoundVariant().deathSound().value();
    }
}
