package com.otterly76.ott.worldgen.structure;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.worldgen.placementcondition.PlacementCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DelegatingStructure extends Structure {
    public static final MapCodec<DelegatingStructure> CODEC;
    public static final StructureType<DelegatingStructure> TYPE;
    private final DelegatingConfig config;

    public DelegatingStructure(DelegatingConfig config) {
        super(createSettings(config));
        this.config = config;
    }

    public DelegatingConfig config() {
        return this.config;
    }

    public Structure delegate() {
        return this.config.delegate().value();
    }

    public @NotNull Optional<Structure.GenerationStub> findValidGenerationPoint(Structure.@NotNull GenerationContext context) {
        return this.findGenerationPoint(context).filter((generationPoint) -> this.isValid(generationPoint, context));
    }

    private boolean isValid(Structure.GenerationStub stub, Structure.GenerationContext context) {
        BlockPos pos = stub.position();
        return context.validBiome().test(context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ()), context.randomState().sampler())) && this.config.spawnCondition().test(PlacementCondition.Context.create(context), pos);
    }

    protected @NotNull Optional<Structure.GenerationStub> findGenerationPoint(Structure.@NotNull GenerationContext context) {
        return this.delegate().findValidGenerationPoint(context);
    }

    public void afterPlace(@NotNull WorldGenLevel level, @NotNull StructureManager structureManager, @NotNull ChunkGenerator generator, @NotNull RandomSource random, @NotNull BoundingBox box, @NotNull ChunkPos chunkPos, @NotNull PiecesContainer container) {
        this.delegate().afterPlace(level, structureManager, generator, random, box, chunkPos, container);
    }

    public @NotNull StructureType<?> type() {
        return TYPE;
    }

    private static Structure.StructureSettings createSettings(DelegatingConfig config) {
        Structure delegate = config.delegate().value();
        return new Structure.StructureSettings(delegate.biomes(), delegate.spawnOverrides(), delegate.step(), delegate.terrainAdaptation());
    }

    static {
        CODEC = DelegatingConfig.CODEC.xmap(DelegatingStructure::new, DelegatingStructure::config);
        TYPE = () -> CODEC;
    }
}
