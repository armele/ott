package com.otterly76.ott.worldgen.structure.processor.condition;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.OttCodecs;
import com.otterly76.ott.worldgen.structure.processor.enums.BlockType;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;

public record MatchingBlocks(HolderSet<Block> blocks, StatePropertiesPredicate properties, BlockType matchType) implements ProcessorCondition {
    private static final StatePropertiesPredicate DEFAULT_PREDICATE = new StatePropertiesPredicate(List.of());
    public static final MapCodec<MatchingBlocks> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(OttCodecs.BLOCK_SET.fieldOf("blocks").forGetter(MatchingBlocks::blocks), StatePropertiesPredicate.CODEC.fieldOf("properties").orElse(DEFAULT_PREDICATE).forGetter(MatchingBlocks::properties), BlockType.CODEC.fieldOf("match_type").orElse(BlockType.INPUT).forGetter(MatchingBlocks::matchType)).apply(instance, MatchingBlocks::new));

    public boolean test(WorldGenLevel level, ProcessorCondition.Data data, StructurePlaceSettings settings, RandomSource random) {
        BlockState state = this.matchType.state(data);
        return state.is(this.blocks) && this.properties.matches(state);
    }

    public MapCodec<? extends ProcessorCondition> codec() {
        return CODEC;
    }
}





