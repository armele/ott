package com.otterly76.ott.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;
import terrablender.api.ParameterUtils.Continentalness;
import terrablender.api.ParameterUtils.Depth;
import terrablender.api.ParameterUtils.Erosion;
import terrablender.api.ParameterUtils.Humidity;
import terrablender.api.ParameterUtils.Temperature;
import terrablender.api.ParameterUtils.Weirdness;

public class ModOverworldRegion extends Region {
    public ModOverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        (new ParameterUtils.ParameterPointListBuilder()).temperature(new ParameterUtils.Temperature[]{Temperature.WARM}).humidity(new ParameterUtils.Humidity[]{Humidity.HUMID}).continentalness(new ParameterUtils.Continentalness[]{Continentalness.INLAND}).erosion(new ParameterUtils.Erosion[]{Erosion.EROSION_2, Erosion.EROSION_3}).depth(new ParameterUtils.Depth[]{Depth.SURFACE}).weirdness(new ParameterUtils.Weirdness[]{Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING}).build().forEach((point) -> builder.add(point, ModBiomes.PALE_GARDEN));
        builder.build().forEach(mapper);
    }
}