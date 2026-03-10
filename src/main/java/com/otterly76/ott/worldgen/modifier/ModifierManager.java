package com.otterly76.ott.worldgen.modifier;

import com.otterly76.ott.registry.OttRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ModifierManager {
    public static void applyModifiers(MinecraftServer server) {
        RegistryAccess registries = server.registryAccess();
        HolderLookup.RegistryLookup<Modifier> modifiers = registries.lookupOrThrow(OttRegistryKeys.WORLDGEN_MODIFIER);

        for (Holder.Reference<Modifier> reference : sortByPriority(modifiers.listElements())) {
            reference.value().applyModifier(registries);
        }
    }

    static List<Holder.Reference<Modifier>> sortByPriority(Stream<Holder.Reference<Modifier>> modifiers) {
        return modifiers.sorted(Comparator.comparingInt((reference) -> reference.value().priority())).toList();
    }
}