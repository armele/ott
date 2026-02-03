package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.mixin.common.HolderReferenceAccessor;
import com.otterly76.ott.worldgen.modifier.util.DensityFunctionWrapper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record WrapDensityFunctionModifier(int priority, Holder<DensityFunction> targetFunction, Holder<DensityFunction> wrapperFunction) implements Modifier {
    private static final Codec<Holder<DensityFunction>> DF_REFERENCE_CODEC;
    public static final MapCodec<WrapDensityFunctionModifier> CODEC;

    public void applyModifier() {
        if (this.targetFunction instanceof Holder.Reference<DensityFunction> reference) {
            // Use (Object) bridge to bypass visibility check on the generic accessor
            @SuppressWarnings("unchecked")
            HolderReferenceAccessor<DensityFunction> accessor = (HolderReferenceAccessor<DensityFunction>) reference;

            accessor.setValue(DensityFunctionWrapper.wrap(this.targetFunction.value(), this.wrapperFunction.value()));
        }
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }

    static {
        DF_REFERENCE_CODEC = RegistryFileCodec.create(Registries.DENSITY_FUNCTION, DensityFunction.DIRECT_CODEC, false);
        CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_DEFAULT.forGetter(WrapDensityFunctionModifier::priority), DF_REFERENCE_CODEC.fieldOf("target_function").forGetter(WrapDensityFunctionModifier::targetFunction), DensityFunction.CODEC.fieldOf("wrapper_function").forGetter(WrapDensityFunctionModifier::wrapperFunction)).apply(instance, WrapDensityFunctionModifier::new));
    }
}