package com.otterly76.ott.mixin.common.mnbs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.duck.mnbs.MNBSPL;
import com.otterly76.ott.util.data.CodecExtender;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.ParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;

@Mixin({MultiNoiseBiomeSourceParameterList.class})
public abstract class MNBSPLMixin implements MNBSPL {
    @Shadow
    @Mutable
    @Final
    private Climate.ParameterList<Holder<Biome>> parameters;

    @Unique
    @Nullable
    private Holder<Biome> ott$migrationBiome;

    @Override
    public void ott$setParameters(Climate.ParameterList<Holder<Biome>> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void ott$setMigrationBiome(@Nullable Holder<Biome> biome) {
        this.ott$migrationBiome = biome;
    }

    @Override
    public void ott$clearMigrationBiome() {
        this.ott$migrationBiome = null;
    }

    @Override
    public Optional<Holder<Biome>> ott$getMigrationBiome() {
        return Optional.ofNullable(this.ott$migrationBiome);
    }

    @WrapOperation(
            method = {"<clinit>()V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
            )
    )
    private static Codec<MultiNoiseBiomeSourceParameterList> wrapCodec(Function<RecordCodecBuilder.Instance<MultiNoiseBiomeSourceParameterList>, ? extends App<RecordCodecBuilder.Mu<MultiNoiseBiomeSourceParameterList>, MultiNoiseBiomeSourceParameterList>> builder, Operation<Codec<MultiNoiseBiomeSourceParameterList>> originalCall) {
        return CodecExtender.extend(originalCall.call(builder), (instance, wrapper) -> instance.group(wrapper, ParameterList.codec(Biome.CODEC.fieldOf("biome")).optionalFieldOf("ott:biomes").forGetter((mnbspl) -> Optional.of(mnbspl.parameters())), Biome.CODEC.optionalFieldOf("ott:migration_biome").forGetter((mnbspl) -> ((MNBSPL)mnbspl).ott$getMigrationBiome())).apply(instance, (mnbspl, parameters, biomeOpt) -> {
            MNBSPL duck = (MNBSPL)mnbspl;
            Objects.requireNonNull(duck);
            parameters.ifPresent(extraParams -> {
                List<Pair<Climate.ParameterPoint, Holder<Biome>>> combined = new ArrayList<>(mnbspl.parameters().values());
                combined.addAll(extraParams.values());
                duck.ott$setParameters(new Climate.ParameterList<>(combined));
            });

            // Resolve the Optional here to satisfy the setter's type requirement
            duck.ott$setMigrationBiome(biomeOpt.orElse(null));

            return mnbspl;
        }));
    }
}
