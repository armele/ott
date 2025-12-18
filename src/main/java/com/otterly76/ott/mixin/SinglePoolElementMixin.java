package com.otterly76.ott.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePoolElement.class)
public abstract class SinglePoolElementMixin {

    @Unique
    private static final int MAX_HEIGHT_DIFFERENCE = 5;

    /**
     * Patch concrete jigsaw pieces (houses/roads) to check for flatness and water.
     */
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void ott$validatePiecePlacement(
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
        // 1. BIOME CHECK (Corners of the building)
        int[] xCoords = {box.minX(), box.maxX()};
        int[] zCoords = {box.minZ(), box.maxZ()};

        for (int x : xCoords) {
            for (int z : zCoords) {
                // Safe noise biome check
                Holder<Biome> biome = level.getUncachedNoiseBiome(x >> 2, pos.getY() >> 2, z >> 2);
                if (biome.is(BiomeTags.IS_RIVER) || biome.is(BiomeTags.IS_OCEAN)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }

        // 2. FLATNESS CHECK (Corners of the building)
        // We use level.getHeight which is safe during the PLACEMENT phase.
        int h1 = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, box.minX(), box.minZ());
        int h2 = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, box.maxX(), box.minZ());
        int h3 = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, box.minX(), box.maxZ());
        int h4 = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, box.maxX(), box.maxZ());

        int minH = Math.min(Math.min(h1, h2), Math.min(h3, h4));
        int maxH = Math.max(Math.max(h1, h2), Math.max(h3, h4));

        if ((maxH - minH) > MAX_HEIGHT_DIFFERENCE) {
            // Building is on a cliff! Cancel it.
            cir.setReturnValue(false);
        }
    }
}