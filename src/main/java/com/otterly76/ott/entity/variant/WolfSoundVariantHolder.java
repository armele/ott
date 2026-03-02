package com.otterly76.ott.entity.variant;

import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.Nullable;

public interface WolfSoundVariantHolder {
    static @Nullable WolfSoundVariantHolder of(Wolf wolf) {
        if (wolf instanceof WolfSoundVariantHolder holder) {
            return holder;
        } else {
            return null;
        }
    }

    WolfSoundVariant ott$getSoundVariant();

    void ott$setSoundVariant(WolfSoundVariant variant);
}
