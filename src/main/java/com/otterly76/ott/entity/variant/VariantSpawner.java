package com.otterly76.ott.entity.variant;

import com.otterly76.ott.config.OttConfig;

@FunctionalInterface
public interface VariantSpawner {
    VariantSpawner DEFAULT = () -> true;
    VariantSpawner FARM_ANIMALS = OttConfig.GENERAL.HAS_FARM_ANIMAL_VARIANTS::get;
    VariantSpawner MONSTERS = OttConfig.GENERAL.HAS_MONSTER_VARIANTS::get;
    VariantSpawner ALLAY = OttConfig.GENERAL.HAS_ALLAY_VARIANTS::get;
    VariantSpawner VEX = OttConfig.GENERAL.HAS_VEX_VARIANTS::get;
    VariantSpawner RABBIT = OttConfig.GENERAL.HAS_RABBIT_VARIANTS::get;

    boolean apply();
}
