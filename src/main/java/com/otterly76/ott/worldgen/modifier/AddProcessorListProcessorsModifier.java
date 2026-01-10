package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.mixin.common.StructureProcessorListAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.ArrayList;
import java.util.List;

public record AddProcessorListProcessorsModifier(int priority, HolderSet<StructureProcessorList> processorLists, StructureProcessorList processors) implements Modifier {
    public static final MapCodec<AddProcessorListProcessorsModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_DEFAULT.forGetter(AddProcessorListProcessorsModifier::priority), OttCodecs.registrySet(Registries.PROCESSOR_LIST, "processor_lists").forGetter(AddProcessorListProcessorsModifier::processorLists), StructureProcessorType.LIST_OBJECT_CODEC.fieldOf("processors").forGetter(AddProcessorListProcessorsModifier::processors)).apply(instance, AddProcessorListProcessorsModifier::new));

    public void applyModifier() {
        this.processorLists.stream().map(Holder::value).forEach(this::applyModifier);
    }

    public void applyModifier(StructureProcessorList processorList) {
        StructureProcessorListAccessor accessor = (StructureProcessorListAccessor)processorList;
        List<StructureProcessor> structureProcessors = new ArrayList<>(processorList.list());
        structureProcessors.addAll(this.processors.list());
        accessor.setProcessors(structureProcessors);
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}