package com.otterly76.ott.worldgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.util.weighted.Weighted;
import com.otterly76.ott.util.weighted.WeightedList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApplyRandomStructureProcessor extends StructureProcessor {
    private static final Codec<WeightedList<Holder<StructureProcessorList>>> WEIGHTED_LIST_CODEC;
    private static final Codec<HolderSet<StructureProcessorList>> SET_CODEC;
    public static final MapCodec<ApplyRandomStructureProcessor> CODEC;
    public static final StructureProcessorType<ApplyRandomStructureProcessor> TYPE;

    private final HolderSet<StructureProcessorList> processorLists;
    private final RandomSettings randomSettings;

    private static HolderSet<StructureProcessorList> convertToSet(WeightedList<Holder<StructureProcessorList>> weightedList) {
        List<Holder<StructureProcessorList>> holders = new ArrayList<>();
        for(Weighted<Holder<StructureProcessorList>> processor : weightedList.unwrap()) {
            for(int i = 0; i < processor.weight(); ++i) {
                holders.add(processor.value());
            }
        }
        return HolderSet.direct(holders);
    }

    public ApplyRandomStructureProcessor(HolderSet<StructureProcessorList> processorLists, RandomSettings randomSettings) {
        this.processorLists = processorLists;
        this.randomSettings = randomSettings;
    }

    public HolderSet<StructureProcessorList> processorLists() {
        return this.processorLists;
    }

    public RandomSettings randomSettings() {
        return this.randomSettings;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockPos pivot, StructureTemplate.@NotNull StructureBlockInfo relative, StructureTemplate.@NotNull StructureBlockInfo absolute, @NotNull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (levelReader instanceof WorldGenLevel level) {
            RandomSource random = this.randomSettings.create(level, pos, absolute);
            Optional<Holder<StructureProcessorList>> processorList = this.processorLists.getRandomElement(random);
            if (processorList.isPresent()) {
                StructureTemplate.StructureBlockInfo processedBlock = absolute;
                for(StructureProcessor processor : processorList.get().value().list()) {
                    processedBlock = processor.process(levelReader, pos, pivot, relative, processedBlock, settings, template);
                    if (processedBlock == null) {
                        break;
                    }
                }
                return processedBlock;
            }
        }
        return absolute;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return TYPE;
    }

    static {
        WEIGHTED_LIST_CODEC = WeightedList.codec(StructureProcessorType.LIST_CODEC);
        SET_CODEC = RegistryCodecs.homogeneousList(Registries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC);
        CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                Codec.withAlternative(SET_CODEC, WEIGHTED_LIST_CODEC, ApplyRandomStructureProcessor::convertToSet).fieldOf("processor_lists").forGetter(ApplyRandomStructureProcessor::processorLists),
                RandomSettings.CODEC.fieldOf("mode").forGetter(ApplyRandomStructureProcessor::randomSettings)
        ).apply(instance, ApplyRandomStructureProcessor::new));
        TYPE = () -> CODEC;
    }
}
