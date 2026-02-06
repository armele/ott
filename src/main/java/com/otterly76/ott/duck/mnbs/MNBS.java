package com.otterly76.ott.duck.mnbs;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

public interface MNBS {
    Either<Climate.ParameterList<Holder<Biome>>, Holder<MNBSPL>> ott$getEntries();

    void ott$setEntries(Either<Climate.ParameterList<Holder<Biome>>, Holder<MNBSPL>> either);
}
