package com.otterly76.ott.duck;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.SurfaceSystem;

public interface ContextAccessor {
    SurfaceSystem ott$getSystem();

    ChunkAccess ott$getChunk();

    Holder<Biome> ott$getBiome();

    int ott$getStoneDepthBelow();

    int ott$getX();

    int ott$getY();

    int ott$getZ();
}
