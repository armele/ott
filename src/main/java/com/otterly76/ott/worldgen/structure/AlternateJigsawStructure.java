package com.otterly76.ott.worldgen.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class AlternateJigsawStructure extends Structure {
    public static final MapCodec<AlternateJigsawStructure> CODEC = RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<AlternateJigsawStructure> instance) -> instance.group(
            settingsCodec(instance),
            AlternateJigsawConfig.CODEC.forGetter(AlternateJigsawStructure::config)
    ).apply(instance, AlternateJigsawStructure::new));
    
    public static final StructureType<AlternateJigsawStructure> TYPE = () -> CODEC;
    private AlternateJigsawConfig config;

    protected AlternateJigsawStructure(Structure.StructureSettings settings, AlternateJigsawConfig config) {
        super(settings);
        this.config = config;
    }

    public void setPoolAliases(List<PoolAliasBinding> poolAliases, boolean append) {
        this.config = this.config.setPoolAliases(poolAliases, append);
    }

    public AlternateJigsawConfig config() {
        return this.config;
    }

    @Override
    public @NotNull Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int i = this.config.startHeight().sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), i, chunkPos.getMinBlockZ());

        int size = this.config.size().sample(context.random());
        PoolAliasLookup aliasLookup = PoolAliasLookup.create(this.config.poolAliases(), blockPos, context.seed());

        BlockPos computedPos = blockPos;
        Optional<Heightmap.Types> finalHeightmapProjection = Optional.empty();

        if (this.config.startProjection().isPresent()) {
            int computedY = this.config.startProjection().get().map(
                    (snap) -> snap.findY(blockPos, context, context.heightAccessor(), context.randomState()),
                    (type) -> Optional.of(blockPos.getY() + context.chunkGenerator().getFirstFreeHeight(blockPos.getX(), blockPos.getZ(), type, context.heightAccessor(), context.randomState()))
            ).orElse(blockPos.getY());
            computedPos = new BlockPos(blockPos.getX(), computedY, blockPos.getZ());
        }

        return JigsawPlacement.addPieces(
                context,
                this.config.startPool(),
                this.config.startJigsawName(),
                size,
                computedPos,
                this.config.useExpansionHack(),
                finalHeightmapProjection,
                this.config.maxDistanceFromCenter().horizontal(),
                aliasLookup,
                this.config.dimensionPadding(),
                this.config.liquidSettings()
        );
    }

    public @NotNull StructureType<?> type() {
        return TYPE;
    }
}