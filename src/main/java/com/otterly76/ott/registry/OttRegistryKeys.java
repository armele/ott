package com.otterly76.ott.registry;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.Ott;
import com.otterly76.ott.worldgen.bandlands.Bandlands;
import com.otterly76.ott.worldgen.bandlands.band.Band;
import com.otterly76.ott.worldgen.modifier.Modifier;
import com.otterly76.ott.worldgen.modifier.template.TemplateList;
import com.otterly76.ott.worldgen.placementcondition.PlacementCondition;
import com.otterly76.ott.worldgen.processor.condition.ProcessorCondition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.SurfaceRules;

public interface OttRegistryKeys {
    ResourceKey<Registry<Modifier>> WORLDGEN_MODIFIER = create("worldgen_modifier");
    ResourceKey<Registry<SurfaceRules.RuleSource>> SURFACE_RULE = create("surface_rule");
    ResourceKey<Registry<Bandlands>> BANDLANDS = create("bandlands");
    ResourceKey<Registry<TemplateList>> TEMPLATE_LIST = create("template_list");
    ResourceKey<Registry<MapCodec<? extends Modifier>>> MODIFIER_TYPE = create("modifier_type");
    ResourceKey<Registry<MapCodec<? extends PlacementCondition>>> PLACEMENT_CONDITION_TYPE = create("placement_condition_type");
    ResourceKey<Registry<MapCodec<? extends ProcessorCondition>>> PROCESSOR_CONDITION_TYPE = create("processor_condition_type");
    ResourceKey<Registry<MapCodec<? extends Band>>> BANDLANDS_BAND_TYPE = create("bandlands_band_type");

    private static <T> ResourceKey<Registry<T>> create(String name) {
        return ResourceKey.createRegistryKey(Ott.resource(name));
    }
}