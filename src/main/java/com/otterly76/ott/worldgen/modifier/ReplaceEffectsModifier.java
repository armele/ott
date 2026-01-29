package com.otterly76.ott.worldgen.modifier;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.neoforge.impl.registry.OttNeoforgeBiomeModifiers;
import com.otterly76.ott.worldgen.biome.BiomeEffects;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;

public class ReplaceEffectsModifier extends AbstractBiomeModifier {
    public static final MapCodec<ReplaceEffectsModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(ReplaceEffectsModifier::biomes), BiomeEffects.CODEC.fieldOf("effects").forGetter(ReplaceEffectsModifier::effects)).apply(instance, ReplaceEffectsModifier::new));
    private final HolderSet<Biome> biomes;
    private final BiomeEffects effects;

    public ReplaceEffectsModifier(HolderSet<Biome> biomes, BiomeEffects effects) {
        super(new OttNeoforgeBiomeModifiers.ReplaceEffectsBiomeModifier(biomes, effects));
        this.biomes = biomes;
        this.effects = effects;
    }

    public HolderSet<Biome> biomes() {
        return this.biomes;
    }

    public BiomeEffects effects() {
        return this.effects;
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}





