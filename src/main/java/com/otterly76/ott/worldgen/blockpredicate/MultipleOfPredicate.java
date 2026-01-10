package com.otterly76.ott.worldgen.blockpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record MultipleOfPredicate(List<BlockPredicate> predicates, InclusiveRange<Integer> allowedCount) implements BlockPredicate {
    public static final MapCodec<MultipleOfPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(BlockPredicate.CODEC.listOf().fieldOf("predicates").forGetter(MultipleOfPredicate::predicates), OttCodecs.INT_RANGE.fieldOf("allowed_count").forGetter(MultipleOfPredicate::allowedCount)).apply(instance, MultipleOfPredicate::new));
    public static final BlockPredicateType<MultipleOfPredicate> TYPE = () -> CODEC;

    public boolean test(WorldGenLevel level, BlockPos pos) {
        int count = 0;

        for(BlockPredicate predicate : this.predicates) {
            if (predicate.test(level, pos)) {
                ++count;
                if (this.allowedCount.maxInclusive() < count) {
                    return false;
                }
            }
        }

        return this.allowedCount.isValueInRange(count);
    }

    public @NotNull BlockPredicateType<?> type() {
        return TYPE;
    }
}