package com.otterly76.ott.mixin.common.mnbs;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.util.CodecExtender;
import com.otterly76.ott.duck.mnbs.MNBS;
import com.otterly76.ott.duck.mnbs.MNBSPL;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin({MultiNoiseBiomeSource.class})
public abstract class MNBSMixin implements MNBS {
    @Shadow
    @Mutable
    @Final
    private Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> parameters;

    @SuppressWarnings("unchecked")
    @Override
    public Either<Climate.ParameterList<Holder<Biome>>, Holder<MNBSPL>> ott$getEntries() {
        return (Either<Climate.ParameterList<Holder<Biome>>, Holder<MNBSPL>>) (Object) this.parameters;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void ott$setEntries(Either<Climate.ParameterList<Holder<Biome>>, Holder<MNBSPL>> entries) {
        this.parameters = (Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>>) (Object) entries;
    }

    @Redirect(
            method = {"<clinit>()V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/MapCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;"
            )
    )
    private static MapCodec<MultiNoiseBiomeSource> wrapCodec(MapCodec<Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>>> original, Function<? super Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>>, ? extends MultiNoiseBiomeSource> to, Function<? super MultiNoiseBiomeSource, ? extends Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>>> from) {
        return CodecExtender.extend(original.xmap(to, from), (instance, wrapper) -> instance.group(wrapper, RegistryOps.retrieveGetter(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)).apply(instance, (mnbs, lookup) -> {
            MNBS duck = (MNBS) mnbs;
            Either<Climate.ParameterList<Holder<Biome>>, Holder<MNBSPL>> biomeEntries = duck.ott$getEntries();

            biomeEntries.left().ifPresent(paramList -> {
                List<Pair<Climate.ParameterPoint, Holder<Biome>>> rawEntries = paramList.values();
                Optional<Holder.Reference<MultiNoiseBiomeSourceParameterList>> overworldPreset = lookup.get(MultiNoiseBiomeSourceParameterLists.OVERWORLD);

                if (overworldPreset.isPresent()) {
                    MNBSPL presetDuck = (MNBSPL) overworldPreset.get().value();
                    Optional<Holder<Biome>> migrationBiome = presetDuck.ott$getMigrationBiome();

                    if (migrationBiome.isPresent()) {
                        Holder<Biome> lastBiome = rawEntries.getLast().getSecond();

                        if (lastBiome.value().equals(migrationBiome.get().value())) {

                            @SuppressWarnings("unchecked")
                            Holder<MNBSPL> holderDuck = (Holder<MNBSPL>) (Object) overworldPreset.get();
                            duck.ott$setEntries(Either.right(holderDuck));
                        }
                    }
                }
            });

            return mnbs;
        }));
    }
}
