package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.worldgen.ModConfiguredFeatures;
import com.otterly76.ott.worldgen.ModPlacedFeatures;
import com.otterly76.ott.worldgen.biome.ModBiomes;
import com.otterly76.ott.worldgen.feature.SpringToLifeFeatures;
import com.otterly76.ott.worldgen.feature.SpringToLifePlacements;
import com.otterly76.ott.worldgen.feature.TheGardenAwakensFeatures;
import com.otterly76.ott.worldgen.feature.TheGardenAwakensPlacements;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class OttWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, context -> {
                ModConfiguredFeatures.bootstrap(context);
                SpringToLifeFeatures.bootstrap(context);
                TheGardenAwakensFeatures.bootstrap(context);
            })
            .add(Registries.PLACED_FEATURE, context -> {
                ModPlacedFeatures.bootstrap(context);
                SpringToLifePlacements.bootstrap(context);
                TheGardenAwakensPlacements.bootstrap(context);
            })
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(Registries.TRIM_MATERIAL, com.otterly76.ott.trim.ModTrimMaterials::bootstrap);

    public OttWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Constants.MOD_ID, "minecraft"));
    }
}