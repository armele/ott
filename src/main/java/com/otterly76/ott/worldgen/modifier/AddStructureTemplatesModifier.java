package com.otterly76.ott.worldgen.modifier;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.api.registry.OttRegistryKeys;
import com.otterly76.ott.worldgen.OttCodecs;
import com.otterly76.ott.worldgen.modifier.template.TemplateList;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record AddStructureTemplatesModifier(int priority, HolderSet<TemplateList> targets, List<ResourceLocation> templates) implements Modifier {
    public static final MapCodec<AddStructureTemplatesModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_DEFAULT.forGetter(AddStructureTemplatesModifier::priority), RegistryCodecs.homogeneousList(OttRegistryKeys.TEMPLATE_LIST).fieldOf("targets").forGetter(AddStructureTemplatesModifier::targets), OttCodecs.compactList(ResourceLocation.CODEC).fieldOf("templates").forGetter(AddStructureTemplatesModifier::templates)).apply(instance, AddStructureTemplatesModifier::new));

    public void applyModifier() {
        this.targets.forEach((holder) -> holder.value().addAll(this.templates));
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}

