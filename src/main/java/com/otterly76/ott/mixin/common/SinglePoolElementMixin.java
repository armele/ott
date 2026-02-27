package com.otterly76.ott.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(SinglePoolElement.class)
public abstract class SinglePoolElementMixin {

    @Unique
    private static final int MAX_H_DIFF = 12;

    /**
     * Deterministic validation using the building's ACTUAL NBT size.
     * Uses the Invoker from SinglePoolElementAccessor to avoid @Shadow issues.
     */
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void ott$validateTrueFootprint(
            StructureTemplateManager templateManager,
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            BlockPos pos,
            BlockPos offset,
            Rotation rotation,
            BoundingBox box,
            RandomSource random,
            LiquidSettings liquidSettings,
            boolean flag,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!(level.getChunkSource() instanceof ServerChunkCache scc)) return;
        RandomState rs = scc.randomState();

        var templateEither = ((SinglePoolElementAccessor) this).getTemplate();

        // Use getOrCreate() to resolve the template from the manager
        StructureTemplate strucTemplate = templateEither.map(
                templateManager::getOrCreate,
                Function.identity()
        );

        Vec3i size = strucTemplate.getSize(rotation);

        int xStart = pos.getX();
        int zStart = pos.getZ();
        int xEnd = xStart + size.getX() - 1;
        int zEnd = zStart + size.getZ() - 1;
        int midX = (xStart + xEnd) / 2;
        int midZ = (zStart + zEnd) / 2;

        int anchorH = generator.getFirstFreeHeight(xStart, zStart, Heightmap.Types.WORLD_SURFACE_WG, level, rs);

        int[][] footprint = {
                {xStart, zStart}, {xEnd, zStart}, {xStart, zEnd}, {xEnd, zEnd},
                {midX, zStart}, {midX, zEnd}, {xStart, midZ}, {xEnd, midZ}
        };

        for (int[] pt : footprint) {
            int ptH = generator.getFirstFreeHeight(pt[0], pt[1], Heightmap.Types.WORLD_SURFACE_WG, level, rs);
            if (Math.abs(ptH - anchorH) > MAX_H_DIFF) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}