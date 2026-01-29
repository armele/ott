package com.otterly76.ott.worldgen.block.predicate;


import com.mojang.serialization.MapCodec;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import org.jetbrains.annotations.NotNull;

public record RandomChancePredicate(float chance) implements BlockPredicate {
    public static final MapCodec<RandomChancePredicate> CODEC;
    public static final BlockPredicateType<RandomChancePredicate> TYPE;

    public boolean test(WorldGenLevel level, BlockPos pos) {
        RandomSource random = RandomSource.create(level.getSeed()).forkPositional().at(pos);
        return random.nextFloat() < this.chance;
    }

    public @NotNull BlockPredicateType<?> type() {
        return TYPE;
    }

    static {
        CODEC = OttCodecs.CHANCE.xmap(RandomChancePredicate::new, RandomChancePredicate::chance);
        TYPE = () -> CODEC;
    }
}

