package com.otterly76.ott.mixin.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Forces Block.shouldRenderFace to return true whenever the neighbor is any skull block,
 * preventing skull collision shapes (including the full-cube override from WallSkullBlockMixin)
 * from culling adjacent block faces.
 */
@Mixin(Block.class)
public class SkullFaceMixin {

    @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
    private static void ott$noSkullOcclusion(
            @NotNull BlockState state, @NotNull BlockGetter level,
            @NotNull BlockPos pos, @NotNull Direction dir,
            @NotNull BlockPos neighborPos,
            @NotNull CallbackInfoReturnable<Boolean> cir) {
        if (level.getBlockState(neighborPos).getBlock() instanceof AbstractSkullBlock) {
            cir.setReturnValue(true);
        }
    }
}
