package com.otterly76.ott.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevents huge mushrooms from spawning within 3 blocks of a chunk boundary.
 *
 * <p>Large mushroom caps extend up to 3 blocks in every horizontal direction. When the
 * mushroom stem is placed near a chunk edge, cap blocks that fall into a neighbouring
 * chunk that has already completed its features stage are silently dropped, leaving a
 * visually incomplete crown. Rejecting the placement entirely (returning {@code false})
 * is preferable to a partial mushroom — the feature will simply try a different position
 * in the next eligible chunk.
 */
@Mixin(AbstractHugeMushroomFeature.class)
public class HugeMushroomFeatureMixin {

    /** Blocks from a chunk edge at which placement is rejected. Equals the cap radius. */
    @Unique
    private static final int EDGE_MARGIN = 3;

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void ott$avoidChunkEdge(FeaturePlaceContext<HugeMushroomFeatureConfiguration> context,
                                    CallbackInfoReturnable<Boolean> cir) {
        BlockPos origin = context.origin();
        int chunkX = origin.getX() & 15;
        int chunkZ = origin.getZ() & 15;
        if (chunkX < EDGE_MARGIN || chunkX >= 16 - EDGE_MARGIN
                || chunkZ < EDGE_MARGIN || chunkZ >= 16 - EDGE_MARGIN) {
            cir.setReturnValue(false);
        }
    }
}