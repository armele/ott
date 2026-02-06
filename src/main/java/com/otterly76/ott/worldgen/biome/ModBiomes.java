package com.otterly76.ott.worldgen.biome;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> PALE_GARDEN = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("minecraft", "pale_garden"));
    public static final ResourceKey<Biome> VERDANT_FOREST = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "verdant_forest"));
    public static final ResourceKey<Biome> FLATNESS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flatness"));
    public static final ResourceKey<Biome> FLATNESS_WATER = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flatness_water"));
}
