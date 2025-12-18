package com.otterly76.ott.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(JigsawStructure.class)
public abstract class JigsawStructureMixin {

    @Inject(method = "findGenerationPoint", at = @At("RETURN"), cancellable = true)
    private void ott$fixStructureSpillover(Structure.GenerationContext context, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir) {
        cir.getReturnValue().ifPresent(stub -> {
            BlockPos pos = stub.position();

            // 1. Check for Water (Existing Fix)
            NoiseColumn column = context.chunkGenerator().getBaseColumn(pos.getX(), pos.getZ(), context.heightAccessor(), context.randomState());
            if (column.getBlock(pos.getY()).getFluidState().isSource()) {
                cir.setReturnValue(Optional.empty());
                return;
            }

            // 2. Check for "Illegal" Biomes (River/Ocean)
            Holder<Biome> biome = context.biomeSource().getNoiseBiome(pos.getX() >> 2, pos.getY() >> 2, pos.getZ() >> 2, context.randomState().sampler());
            if (biome.is(BiomeTags.IS_RIVER) || biome.is(BiomeTags.IS_OCEAN)) {
                // If the center point is in a river or ocean, don't even start the village.
                cir.setReturnValue(Optional.empty());
            }
        });
    }
}