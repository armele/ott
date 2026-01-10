package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.mixin.common.HolderReferenceAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import com.otterly76.ott.worldgen.feature.CompositeFeature;
import com.otterly76.ott.worldgen.feature.config.CompositeConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public record StackFeatureModifier(int priority, HolderSet<ConfiguredFeature<?, ?>> baseFeatures, Holder<PlacedFeature> stackedFeature, CompositeConfig.Type placementType) implements Modifier {
    public static final MapCodec<StackFeatureModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_DEFAULT.forGetter(StackFeatureModifier::priority), OttCodecs.registrySet(Registries.CONFIGURED_FEATURE, "base_features").forGetter(StackFeatureModifier::baseFeatures), PlacedFeature.CODEC.fieldOf("stacked_feature").forGetter(StackFeatureModifier::stackedFeature), CompositeConfig.Type.CODEC.fieldOf("placement_type").orElse(CompositeConfig.Type.CANCEL_ON_FAILURE).forGetter(StackFeatureModifier::placementType)).apply(instance, StackFeatureModifier::new));

    public void applyModifier() {
        this.baseFeatures.stream().forEach(this::applyModifier);
    }

    private void applyModifier(Holder<ConfiguredFeature<?, ?>> feature) {
        if (feature instanceof Holder.Reference<ConfiguredFeature<?, ?>> reference) {
            // Use (Object) bridge to bypass visibility check on the generic accessor
            @SuppressWarnings("unchecked")
            HolderReferenceAccessor<ConfiguredFeature<?, ?>> accessor = (HolderReferenceAccessor<ConfiguredFeature<?, ?>>) reference;

            accessor.setValue(new ConfiguredFeature<>(
                    CompositeFeature.FEATURE,
                    new CompositeConfig(
                            HolderSet.direct(Holder.direct(new PlacedFeature(Holder.direct(feature.value()), List.of())), this.stackedFeature),
                            this.placementType
                    )
            ));
        }
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}