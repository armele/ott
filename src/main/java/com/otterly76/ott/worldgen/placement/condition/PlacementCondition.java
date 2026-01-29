package com.otterly76.ott.worldgen.placement.condition;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.api.registry.OttRegistryKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.function.Function;

public interface PlacementCondition {
    Codec<PlacementCondition> BASE_CODEC = Codec.lazyInitialized(() -> {
        @SuppressWarnings("unchecked")
        Registry<MapCodec<? extends PlacementCondition>> registry = (Registry<MapCodec<? extends PlacementCondition>>) BuiltInRegistries.REGISTRY
                .getOptional(OttRegistryKeys.PLACEMENT_CONDITION_TYPE.location())
                .orElseThrow(() -> new NullPointerException("Placement condition registry does not exist yet!"));

        return registry.byNameCodec().dispatch(PlacementCondition::codec, Function.identity());
    });

    Codec<PlacementCondition> CODEC = Codec.withAlternative(BASE_CODEC, BASE_CODEC.listOf(), AllOfPlacementCondition::new);

    boolean test(Context context, BlockPos blockPos);

    default boolean test(PlacementContext context, BlockPos pos) {
        return this.test(PlacementCondition.Context.create(context), pos);
    }

    MapCodec<? extends PlacementCondition> codec();

    record Context(RegistryAccess registries, ChunkGenerator generator, LevelHeightAccessor heightAccessor, RandomState randomState, BiomeSource biomeSource, long seed) {
        public static Context create(Structure.GenerationContext context) {
            return new Context(context.registryAccess(), context.chunkGenerator(), context.heightAccessor(), context.randomState(), context.biomeSource(), context.seed());
        }

        private static Context create(PlacementContext context) {
            WorldGenLevel level = context.getLevel();
            return new Context(level.registryAccess(), context.generator(), level, level.getLevel().getChunkSource().randomState(), context.generator().getBiomeSource(), level.getSeed());
        }
    }
}





