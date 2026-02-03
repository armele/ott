package com.otterly76.ott.worldgen.placementcondition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.biome.Biome;

public record InBiomePlacementCondition(HolderSet<Biome> biomes) implements PlacementCondition {
    public static final MapCodec<InBiomePlacementCondition> CODEC;

    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ()), context.randomState().sampler());
        return this.biomes.contains(biome);
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }

    static {
        CODEC = Biome.LIST_CODEC.fieldOf("biomes").xmap(InBiomePlacementCondition::new, InBiomePlacementCondition::biomes);
    }
}