package com.otterly76.ott.entity.variant;

import com.otterly76.ott.config.OttConfig;

@FunctionalInterface
public interface VariantSpawner {
    VariantSpawner DEFAULT = () -> true;
    VariantSpawner FARM_ANIMALS = OttConfig.GENERAL.HAS_FARM_ANIMAL_VARIANTS::get;

    boolean apply();
}
