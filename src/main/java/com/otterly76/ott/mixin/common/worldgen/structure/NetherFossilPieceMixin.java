package com.otterly76.ott.mixin.common.worldgen.structure;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.NetherFossilPieces$NetherFossilPiece")
public abstract class NetherFossilPieceMixin extends TemplateStructurePiece {

    protected NetherFossilPieceMixin(StructurePieceType type, int genDepth, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager templateManager, net.minecraft.resources.ResourceLocation location, String templateName, StructurePlaceSettings placeSettings, BlockPos templatePosition) {
        super(type, genDepth, templateManager, location, templateName, placeSettings, templatePosition);
    }

    @Inject(method = "postProcess", at = @At("TAIL"))
    private void ott$placeDriedGhast(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource worldRandom, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos, CallbackInfo ci) {
        if (OttConfig.WORLDGEN.SPAWN_DRIED_GHASTS.get() && level.dimensionType().ultraWarm() && level.dimensionType().piglinSafe()) {
            TemplateStructurePiece piece = this;
            BoundingBox templateBox = piece.template().getBoundingBox(piece.placeSettings(), piece.templatePosition());
            RandomSource random = RandomSource.create(level.getSeed()).forkPositional().at(templateBox.getCenter());

            if (random.nextFloat() < 0.5f) {
                int xSpan = templateBox.getXSpan();
                int zSpan = templateBox.getZSpan();
                
                int x = templateBox.minX() + (xSpan > 0 ? random.nextInt(xSpan) : 0);
                int y = templateBox.minY();
                int z = templateBox.minZ() + (zSpan > 0 ? random.nextInt(zSpan) : 0);
                BlockPos targetPos = new BlockPos(x, y, z);

                if (level.getBlockState(targetPos).isAir() && box.isInside(targetPos)) {
                    level.setBlock(targetPos, ModBlocks.DRIED_GHAST.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)), 2);
                }
            }
        }
    }
}
