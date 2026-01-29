package com.otterly76.ott.worldgen.structure.processor;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReferenceStructureProcessor extends StructureProcessor {
    public static final MapCodec<ReferenceStructureProcessor> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(RegistryCodecs.homogeneousList(Registries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC).fieldOf("processor_lists").forGetter(ReferenceStructureProcessor::processorLists)).apply(instance, ReferenceStructureProcessor::new));
    public static final StructureProcessorType<ReferenceStructureProcessor> TYPE = () -> CODEC;
    private final HolderSet<StructureProcessorList> processorLists;

    public ReferenceStructureProcessor(HolderSet<StructureProcessorList> processorLists) {
        this.processorLists = processorLists;
    }

    public HolderSet<StructureProcessorList> processorLists() {
        return this.processorLists;
    }

    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockPos pivot, StructureTemplate.@NotNull StructureBlockInfo relative, StructureTemplate.@NotNull StructureBlockInfo absolute, @NotNull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        StructureTemplate.StructureBlockInfo processedBlock = absolute;

        for(Holder<StructureProcessorList> processorList : this.processorLists) {
            for(StructureProcessor processor : processorList.value().list()) {
                processedBlock = processor.process(levelReader, pos, pivot, relative, processedBlock, settings, template);
                if (processedBlock == null) {
                    return null;
                }
            }
        }

        return processedBlock;
    }

    protected @NotNull StructureProcessorType<?> getType() {
        return TYPE;
    }
}

