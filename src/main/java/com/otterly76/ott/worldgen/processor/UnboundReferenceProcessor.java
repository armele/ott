package com.otterly76.ott.worldgen.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.Ott;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UnboundReferenceProcessor extends StructureProcessor {
    public static final MapCodec<UnboundReferenceProcessor> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(ResourceKey.codec(Registries.PROCESSOR_LIST).fieldOf("name").forGetter(UnboundReferenceProcessor::name)).apply(instance, UnboundReferenceProcessor::new));
    public static final StructureProcessorType<UnboundReferenceProcessor> TYPE = () -> CODEC;
    private final ResourceKey<StructureProcessorList> name;

    private UnboundReferenceProcessor(ResourceKey<StructureProcessorList> name) {
        this.name = name;
    }

    public static UnboundReferenceProcessor of(String name) {
        return new UnboundReferenceProcessor(key(Ott.resource(name)));
    }

    private static ResourceKey<StructureProcessorList> key(ResourceLocation identifier) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, identifier);
    }

    public ResourceKey<StructureProcessorList> name() {
        return this.name;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockPos pivot, StructureTemplate.@NotNull StructureBlockInfo relative, StructureTemplate.@NotNull StructureBlockInfo absolute, @NotNull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        StructureTemplate.StructureBlockInfo processedBlock = absolute;
        Registry<StructureProcessorList> registry = Ott.registry(levelReader.registryAccess(), Registries.PROCESSOR_LIST);
        Optional<StructureProcessorList> list = registry.getOptional(this.name);
        if (list.isPresent()) {
            for(StructureProcessor processor : list.get().list()) {
                // Call the correct 'process' method with the template parameter
                processedBlock = processor.process(levelReader, pos, pivot, relative, processedBlock, settings, template);
                if (processedBlock == null) {
                    return null;
                }
            }
        }

        return processedBlock;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return TYPE;
    }
}