package com.otterly76.ott.mixin.common;


import com.otterly76.ott.api.mixin.ContextAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(targets = "net.minecraft.world.level.levelgen.SurfaceRules$Context")
public abstract class SurfaceRulesContextMixin implements ContextAccessor {
    @Shadow @Final SurfaceSystem system;
    @Shadow @Final ChunkAccess chunk;
    @Shadow Supplier<Holder<Biome>> biome;
    @Shadow int blockX;
    @Shadow int blockY;
    @Shadow int blockZ;
    @Shadow int stoneDepthBelow;

    @Override
    public SurfaceSystem ott$getSystem() {
        return this.system;
    }

    @Override
    public ChunkAccess ott$getChunk() {
        return this.chunk;
    }

    @Override
    public Holder<Biome> ott$getBiome() {
        return this.biome.get();
    }

    @Override
    public int ott$getStoneDepthBelow() {
        return this.stoneDepthBelow;
    }

    @Override
    public int ott$getX() {
        return this.blockX;
    }

    @Override
    public int ott$getY() {
        return this.blockY;
    }

    @Override
    public int ott$getZ() {
        return this.blockZ;
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/level/levelgen/SurfaceSystem;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/NoiseChunk;Ljava/util/function/Function;Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/levelgen/WorldGenerationContext;)V",
            at = @At("RETURN")
    )
    private void ott$onInit(SurfaceSystem system, RandomState randomState, ChunkAccess chunk, NoiseChunk noiseChunk, Function<BlockPos, Holder<Biome>> biomeGetter, Registry<Biome> biomeRegistry, WorldGenerationContext context, CallbackInfo ci) {
        // Initialization logic if needed
    }
}
