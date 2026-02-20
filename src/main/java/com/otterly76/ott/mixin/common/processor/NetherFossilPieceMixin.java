package com.otterly76.ott.mixin.common.processor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.config.ConfigHandler;
import com.otterly76.ott.worldgen.processor.UnboundReferenceProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.NetherFossilPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetherFossilPieces.NetherFossilPiece.class})
public abstract class NetherFossilPieceMixin extends TemplateStructurePiece {
    public NetherFossilPieceMixin(StructurePieceType type, int genDepth, StructureTemplateManager structureTemplateManager, ResourceLocation location, String templateName, StructurePlaceSettings placeSettings, BlockPos templatePosition) {
        super(type, genDepth, structureTemplateManager, location, templateName, placeSettings, templatePosition);
    }

    @ModifyReturnValue(
            method = {"makeSettings(Lnet/minecraft/world/level/block/Rotation;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;"},
            at = {@At("RETURN")}
    )
    private static StructurePlaceSettings addShipwreckProcessor(StructurePlaceSettings settings) {
        return ConfigHandler.getConfig().breaksSeedParity() ? settings.addProcessor(UnboundReferenceProcessor.of("nether_fossil")) : settings;
    }

    @Inject(
        method = {"postProcess(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/core/BlockPos;)V"},
        at = {@At("TAIL")}
    )
    private void placeDriedGhast(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource worldRandom, BoundingBox box, ChunkPos chunkPos, BlockPos origin, CallbackInfo ci) {
        if (level.dimensionType().ultraWarm() && level.dimensionType().piglinSafe()) {
            BoundingBox template = this.template().getBoundingBox(this.placeSettings(), this.templatePosition());
            RandomSource random = RandomSource.create(level.getSeed()).forkPositional().at(template.getCenter());
            if (random.nextFloat() < 0.5F) {
                int x = template.minX() + random.nextInt(template.getXSpan());
                int y = template.minY();
                int z = template.minZ() + random.nextInt(template.getZSpan());
                BlockPos pos = new BlockPos(x, y, z);
                if (level.getBlockState(pos).isAir() && box.isInside(pos)) {
                    Rotation rotation = Rotation.getRandom(random);
                    BlockState state = ModBlocks.DRIED_GHAST.get().defaultBlockState();
                    level.setBlock(pos, state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING))), 2);
                }
            }
        }
    }
}