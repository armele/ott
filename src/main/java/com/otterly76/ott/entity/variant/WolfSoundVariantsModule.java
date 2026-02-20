package com.otterly76.ott.entity.variant;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.Nullable;

public class WolfSoundVariantsModule {
    public static <T extends Wolf & WolfSoundVariantHolder> @Nullable SoundEvent getAmbientSound(T wolf) {
        if (!OttConfig.GENERAL.HAS_WOLF_SOUND_VARIANTS.get()) {
            return null;
        } else if (wolf.isAngry()) {
            return wolf.ott$getSoundVariant().growlSound().value();
        } else if (wolf.getRandom().nextInt(3) != 0) {
            return wolf.ott$getSoundVariant().ambientSound().value();
        } else {
            return wolf.isTame() && wolf.getHealth() < 20.0F ? wolf.ott$getSoundVariant().whineSound().value() : wolf.ott$getSoundVariant().pantSound().value();
        }
    }

    public static <T extends Wolf & WolfSoundVariantHolder> @Nullable SoundEvent getHurtSound(T wolf) {
        return !OttConfig.GENERAL.HAS_WOLF_SOUND_VARIANTS.get() ? null : wolf.ott$getSoundVariant().hurtSound().value();
    }

    public static <T extends Wolf & WolfSoundVariantHolder> @Nullable SoundEvent getDeathSound(T wolf) {
        return !OttConfig.GENERAL.HAS_WOLF_SOUND_VARIANTS.get() ? null : wolf.ott$getSoundVariant().deathSound().value();
    }
}