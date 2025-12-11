package com.otterly76.ott.generation;

import com.otterly76.ott.trim.ModTrimMaterials;
import com.otterly76.ott.worldgen.ModConfiguredFeatures;
import com.otterly76.ott.worldgen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER;

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft"));
    }

    static {
        BUILDER = (new RegistrySetBuilder()).add(Registries.TRIM_MATERIAL, ModTrimMaterials::bootstrap).add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap).add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    }
}