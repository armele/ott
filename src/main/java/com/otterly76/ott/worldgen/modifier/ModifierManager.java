package com.otterly76.ott.worldgen.modifier;

import com.google.common.base.Suppliers;
import com.otterly76.ott.Ott;
import com.otterly76.ott.mixin.common.ChunkGeneratorAccessor;
import com.otterly76.ott.api.registry.OttRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ModifierManager {
    public static void applyModifiers(MinecraftServer server) {
        boolean fabricFeaturesModified = false;
        RegistryAccess registries = server.registryAccess();
        HolderLookup.RegistryLookup<Modifier> modifiers = registries.lookupOrThrow(OttRegistryKeys.WORLDGEN_MODIFIER);

        for (Holder.Reference<Modifier> reference : sortByPriority(modifiers.listElements())) {
            reference.value().applyModifier(registries);
            if (reference.value().internal$modifiesFabricFeatures()) {
                fabricFeaturesModified = true;
            }
        }

        if (fabricFeaturesModified) {
            for (LevelStem dimension : Ott.registry(registries, Registries.LEVEL_STEM).stream().toList()) {
                if (dimension.generator() instanceof ChunkGeneratorAccessor accessor) {
                    BiomeSource source = accessor.getBiomeSource();
                    accessor.setFeaturesPerStep(Suppliers.memoize(() ->
                            FeatureSorter.buildFeaturesPerStep(
                                    List.copyOf(source.possibleBiomes()),
                                    (biome) -> accessor.getGetter().apply(biome).features(),
                                    true
                            )
                    ));
                }
            }
        }
    }

    static List<Holder.Reference<Modifier>> sortByPriority(Stream<Holder.Reference<Modifier>> modifiers) {
        return modifiers.sorted(Comparator.comparingInt((reference) -> reference.value().priority())).toList();
    }
}

