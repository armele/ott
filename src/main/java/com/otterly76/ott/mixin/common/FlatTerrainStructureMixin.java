package com.otterly76.ott.mixin.common;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(JigsawStructure.class)
public class FlatTerrainStructureMixin {

    @Unique private static final TagKey<Structure> REQUIRES_FLAT_TERRAIN =
        TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath("ott", "requires_flat_terrain"));

    /** Grid sample: 5×5 points at 8-block intervals → covers a 32×32 block area around the chunk centre */
    @Unique private static final int SAMPLE_STEP   = 8;
    @Unique private static final int SAMPLE_RADIUS = 2;
    /** Maximum allowed surface height variance across the sampled area */
    @Unique private static final int MAX_VARIANCE  = 10;

    @Inject(method = "findGenerationPoint", at = @At("HEAD"), cancellable = true)
    private void ott$requireFlatTerrain(
        @NotNull Structure.GenerationContext context,
        @NotNull CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir
    ) {
        RegistryAccess registryAccess = context.registryAccess();
        Registry<Structure> registry = registryAccess.registryOrThrow(Registries.STRUCTURE);
        Structure self = (Structure)(Object)this;
        Optional<Holder.Reference<Structure>> holderOpt = registry.getResourceKey(self).flatMap(registry::getHolder);
        if (holderOpt.isEmpty() || !holderOpt.get().is(REQUIRES_FLAT_TERRAIN)) return;

        int centerX = context.chunkPos().getMiddleBlockX();
        int centerZ = context.chunkPos().getMiddleBlockZ();
        int minH = Integer.MAX_VALUE;
        int maxH = Integer.MIN_VALUE;

        for (int dx = -SAMPLE_RADIUS; dx <= SAMPLE_RADIUS; dx++) {
            for (int dz = -SAMPLE_RADIUS; dz <= SAMPLE_RADIUS; dz++) {
                int h = context.chunkGenerator().getBaseHeight(
                    centerX + dx * SAMPLE_STEP,
                    centerZ + dz * SAMPLE_STEP,
                    Heightmap.Types.WORLD_SURFACE_WG,
                    context.heightAccessor(),
                    context.randomState()
                );
                if (h < minH) minH = h;
                if (h > maxH) maxH = h;
            }
        }

        if (maxH - minH > MAX_VARIANCE) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
