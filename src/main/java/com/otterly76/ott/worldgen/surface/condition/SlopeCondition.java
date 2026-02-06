package com.otterly76.ott.worldgen.surface.condition;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.duck.ContextAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public record SlopeCondition(InclusiveRange<Integer> threshold) implements SurfaceRules.ConditionSource {
    private static final InclusiveRange<Integer> BASE_DIFFERENCE = new InclusiveRange<>(4, Integer.MAX_VALUE);
    public static final KeyDispatchDataCodec<SlopeCondition> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((instance) -> instance.group(OttCodecs.INT_RANGE.fieldOf("height_difference").orElse(BASE_DIFFERENCE).forGetter(SlopeCondition::threshold)).apply(instance, SlopeCondition::new)));

    @Override
    public @NotNull KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.Condition apply(SurfaceRules.Context context) {
        return new Condition(context, this.threshold);
    }

    private static int operate(int a, int b, int c, int d, BiFunction<Integer, Integer, Integer> operation) {
        return operation.apply(operation.apply(a, b), operation.apply(c, d));
    }

    private static class Condition extends SurfaceRules.LazyXZCondition {
        private final ContextAccessor context;
        private final InclusiveRange<Integer> threshold;

        private Condition(SurfaceRules.Context context, InclusiveRange<Integer> threshold) {
            super(context);
            // Use (Object) bridge to bypass the inconvertible types error
            this.context = (ContextAccessor) (Object) context;
            this.threshold = threshold;
        }

        @Override
        public boolean compute() {
            // Use prefixed methods from the duck interface
            ChunkAccess chunkAccess = this.context.ott$getChunk();
            int x = this.context.ott$getX() & 15;
            int z = this.context.ott$getZ() & 15;

            int north = Math.max(z - 1, 0);
            int south = Math.min(z + 1, 15);
            int west = Math.max(x - 1, 0);
            int east = Math.min(x + 1, 15);

            int northHeight = chunkAccess.getHeight(Types.WORLD_SURFACE_WG, x, north);
            int southHeight = chunkAccess.getHeight(Types.WORLD_SURFACE_WG, x, south);
            int westHeight = chunkAccess.getHeight(Types.WORLD_SURFACE_WG, west, z);
            int eastHeight = chunkAccess.getHeight(Types.WORLD_SURFACE_WG, east, z);

            return this.threshold.isValueInRange(SlopeCondition.operate(northHeight, southHeight, eastHeight, westHeight, Math::max) - SlopeCondition.operate(northHeight, southHeight, eastHeight, westHeight, Math::min));
        }
    }
}
