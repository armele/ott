package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifiers;

public class AddFeaturesModifier extends AbstractBiomeModifier {
    public static final MapCodec<AddFeaturesModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddFeaturesModifier::biomes), PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeaturesModifier::features), Decoration.CODEC.fieldOf("step").forGetter(AddFeaturesModifier::step)).apply(instance, AddFeaturesModifier::new));
    private final HolderSet<Biome> biomes;
    private final HolderSet<PlacedFeature> features;
    private final GenerationStep.Decoration step;

    public AddFeaturesModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, GenerationStep.Decoration step) {
        super(new BiomeModifiers.AddFeaturesBiomeModifier(biomes, features, step));
        this.biomes = biomes;
        this.features = features;
        this.step = step;
    }

    public HolderSet<Biome> biomes() {
        return this.biomes;
    }

    public HolderSet<PlacedFeature> features() {
        return this.features;
    }

    public GenerationStep.Decoration step() {
        return this.step;
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}