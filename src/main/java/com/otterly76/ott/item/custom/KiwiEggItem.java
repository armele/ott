package com.otterly76.ott.item.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.projectile.KiwiEggEntity;

public class KiwiEggItem extends ThrowableEggItem<KiwiEggEntity> {
    public KiwiEggItem(Properties properties) {
        super(properties, ModEntities.KIWI_EGG);
    }
}
