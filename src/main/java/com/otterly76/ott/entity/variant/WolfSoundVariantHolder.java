package com.otterly76.ott.entity.variant;

import net.minecraft.world.entity.animal.Wolf;

public interface WolfSoundVariantHolder {
    static WolfSoundVariantHolder of(Wolf wolf) {
        if (wolf instanceof WolfSoundVariantHolder holder) {
            return holder;
        } else {
            return null;
        }
    }

    WolfSoundVariant getSoundVariant();

    void setSoundVariant(WolfSoundVariant variant);
}
