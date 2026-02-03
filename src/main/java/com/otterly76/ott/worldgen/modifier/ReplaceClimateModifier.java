package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.registry.OttNeoforgeBiomeModifiers;
import com.otterly76.ott.worldgen.modifier.util.BiomeClimate;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;

public class ReplaceClimateModifier extends AbstractBiomeModifier {
    public static final MapCodec<ReplaceClimateModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(ReplaceClimateModifier::biomes), BiomeClimate.CODEC.fieldOf("climate").forGetter(ReplaceClimateModifier::climateSettings)).apply(instance, ReplaceClimateModifier::new));
    private final HolderSet<Biome> biomes;
    private final BiomeClimate climateSettings;

    public ReplaceClimateModifier(HolderSet<Biome> biomes, BiomeClimate climateSettings) {
        super(new OttNeoforgeBiomeModifiers.ReplaceClimateBiomeModifier(biomes, climateSettings));
        this.biomes = biomes;
        this.climateSettings = climateSettings;
    }

    public HolderSet<Biome> biomes() {
        return this.biomes;
    }

    public BiomeClimate climateSettings() {
        return this.climateSettings;
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}