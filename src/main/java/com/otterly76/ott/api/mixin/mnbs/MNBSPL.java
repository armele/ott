package com.otterly76.ott.api.mixin.mnbs;


import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.Optional;

public interface MNBSPL {
    void ott$setParameters(Climate.ParameterList<Holder<Biome>> parameterList);

    void ott$setMigrationBiome(Holder<Biome> biome);

    void ott$clearMigrationBiome();

    Optional<Holder<Biome>> ott$getMigrationBiome();
}
