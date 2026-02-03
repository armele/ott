package com.otterly76.ott.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import com.otterly76.ott.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.ParameterUtils.*;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class ModOverworldRegion extends Region {
    public ModOverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        (new ParameterUtils.ParameterPointListBuilder())
                .temperature(Temperature.WARM)
                .humidity(Humidity.HUMID)
                .continentalness(Continentalness.INLAND)
                .erosion(Erosion.EROSION_2, Erosion.EROSION_3)
                .depth(Depth.SURFACE)
                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                .build().forEach((point) -> builder.add(point, VERDANT_FOREST));
        builder.build().forEach(mapper);
    }

    public static final ResourceKey<Biome> VERDANT_FOREST = ResourceKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "verdant_forest")
    );
}