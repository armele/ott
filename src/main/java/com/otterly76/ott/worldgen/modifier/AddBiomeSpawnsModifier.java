package com.otterly76.ott.worldgen.modifier;


import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.neoforged.neoforge.common.world.BiomeModifiers;

import java.util.List;

public class AddBiomeSpawnsModifier extends AbstractBiomeModifier {
    public static final MapCodec<AddBiomeSpawnsModifier> CODEC = RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<AddBiomeSpawnsModifier> instance) -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddBiomeSpawnsModifier::biomes),
            Codec.mapEither(SpawnerData.CODEC.listOf().fieldOf("spawners"), SpawnerData.CODEC.fieldOf("spawners"))
                    .xmap(either -> either.map(list -> list, List::of), Either::left)
                    .forGetter(AddBiomeSpawnsModifier::biomeSpawns)
    ).apply(instance, AddBiomeSpawnsModifier::new));

    private final HolderSet<Biome> biomes;
    private final List<MobSpawnSettings.SpawnerData> biomeSpawns;

    protected AddBiomeSpawnsModifier(HolderSet<Biome> biomes, List<MobSpawnSettings.SpawnerData> biomeSpawns) {
        super(new BiomeModifiers.AddSpawnsBiomeModifier(biomes, biomeSpawns));
        this.biomes = biomes;
        this.biomeSpawns = biomeSpawns;
    }

    public HolderSet<Biome> biomes() {
        return this.biomes;
    }

    public List<MobSpawnSettings.SpawnerData> biomeSpawns() {
        return this.biomeSpawns;
    }

    @Override
    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}




