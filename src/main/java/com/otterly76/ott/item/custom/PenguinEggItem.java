package com.otterly76.ott.item.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.projectile.PenguinEggEntity;

public class PenguinEggItem extends ThrowableEggItem<PenguinEggEntity> {
    public PenguinEggItem(Properties properties) {
        super(properties, ModEntities.PENGUIN_EGG);
    }
}
