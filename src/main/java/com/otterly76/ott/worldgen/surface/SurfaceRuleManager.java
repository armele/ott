package com.otterly76.ott.worldgen.surface;

import com.mojang.datafixers.util.Pair;
import com.otterly76.ott.Ott;
import com.otterly76.ott.mixin.common.NoiseBasedChunkGeneratorAccessor;
import com.otterly76.ott.registry.OttRegistryKeys;
import com.otterly76.ott.worldgen.modifier.AddSurfaceRuleModifier;
import com.otterly76.ott.worldgen.modifier.Modifier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.*;
import java.util.stream.Collectors;

public class SurfaceRuleManager {
    @SuppressWarnings("deprecation")
    public static void applySurfaceRules(MinecraftServer server) {
        RegistryAccess registries = server.registryAccess();
        Set<Map.Entry<ResourceKey<Modifier>, Modifier>> surfaceRules = Ott.registry(registries, OttRegistryKeys.WORLDGEN_MODIFIER).entrySet().stream().filter((entryx) -> entryx.getValue() instanceof AddSurfaceRuleModifier).collect(Collectors.toSet());
        if (!surfaceRules.isEmpty()) {
            HashMap<ResourceLocation, ArrayList<Pair<ResourceLocation, AddSurfaceRuleModifier>>> assignedSurfaceRules = new HashMap<>();

            for(Map.Entry<ResourceKey<Modifier>, Modifier> assignedSurfaceRule : surfaceRules) {
                AddSurfaceRuleModifier slice = (AddSurfaceRuleModifier)assignedSurfaceRule.getValue();
                slice.levels().forEach((levelStemResourceKey) -> assignedSurfaceRules.computeIfAbsent(levelStemResourceKey.location(), (__) -> new ArrayList<>()).add(Pair.of(assignedSurfaceRule.getKey().location(), slice)));
            }

            Registry<LevelStem> dimensions = Ott.registry(registries, Registries.LEVEL_STEM);

            for(Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : dimensions.entrySet()) {
                ResourceLocation location = entry.getKey().location();
                ArrayList<Pair<ResourceLocation, AddSurfaceRuleModifier>> surfaceRulesForKey = assignedSurfaceRules.get(location);
                if (surfaceRulesForKey != null) {
                    ChunkGenerator var10 = entry.getValue().generator();
                    if (var10 instanceof NoiseBasedChunkGenerator generator) {
                        NoiseGeneratorSettings settings = generator.generatorSettings().value();
                        SurfaceRules.RuleSource oldRules = settings.surfaceRule();

                        ((NoiseBasedChunkGeneratorAccessor)generator).setSettings(Holder.direct(new NoiseGeneratorSettings(
                                settings.noiseSettings(),
                                settings.defaultBlock(),
                                settings.defaultFluid(),
                                settings.noiseRouter(),
                                buildModdedSurfaceRules(surfaceRulesForKey, oldRules),
                                settings.spawnTarget(),
                                settings.seaLevel(),
                                settings.disableMobGeneration(),
                                settings.aquifersEnabled(),
                                settings.oreVeinsEnabled(),
                                settings.useLegacyRandomSource()
                        )));
                    }
                }
            }
        }
    }

    private static SurfaceRules.RuleSource buildModdedSurfaceRules(ArrayList<Pair<ResourceLocation, AddSurfaceRuleModifier>> moddedSourceList, SurfaceRules.RuleSource originalSource) {
        List<SurfaceRules.RuleSource> newRuleSourceList = new ArrayList<>();
        moddedSourceList.sort(Comparator.comparingInt((pair) -> pair.getSecond().priority()));
        moddedSourceList.forEach((pair) -> newRuleSourceList.add(pair.getSecond().surfaceRule()));
        newRuleSourceList.add(originalSource);
        return SurfaceRules.sequence(newRuleSourceList.toArray(SurfaceRules.RuleSource[]::new));
    }
}