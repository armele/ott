package com.otterly76.ott.worldgen.processor.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.registry.OttRegistryKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.function.Function;

public interface ProcessorCondition {
    @SuppressWarnings("unchecked")
    Codec<ProcessorCondition> BASE_CODEC = Codec.lazyInitialized(() -> {
        // Explicitly type the registry to help the compiler resolve the byNameCodec return type
        Registry<MapCodec<? extends ProcessorCondition>> registry = (Registry<MapCodec<? extends ProcessorCondition>>) BuiltInRegistries.REGISTRY
                .getOptional(OttRegistryKeys.PROCESSOR_CONDITION_TYPE.location())
                .orElseThrow(() -> new NullPointerException("Processor condition registry does not exist yet!"));

        return registry.byNameCodec();
    }).dispatch(ProcessorCondition::codec, Function.identity());

    Codec<ProcessorCondition> CODEC = Codec.withAlternative(BASE_CODEC, BASE_CODEC.listOf(), AllOf::new);

    boolean test(WorldGenLevel worldGenLevel, Data data, StructurePlaceSettings structurePlaceSettings, RandomSource randomSource);

    MapCodec<? extends ProcessorCondition> codec();

    record Data(BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo relative, StructureTemplate.StructureBlockInfo absolute) {
    }
}