package com.otterly76.ott.mixin.common;

import com.otterly76.ott.util.lantern.FluidLanternManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowingFluid.class)
public abstract class FlowingFluidMixin {

    @Inject(method = "canSpreadTo", at = @At("HEAD"), cancellable = true)
    private void ott$blockLanternProtectedFluidSpread(BlockGetter level,
                                                      BlockPos fromPos,
                                                      BlockState fromState,
                                                      Direction direction,
                                                      BlockPos toPos,
                                                      BlockState toState,
                                                      FluidState toFluidState,
                                                      Fluid fluid,
                                                      CallbackInfoReturnable<Boolean> cir) {
        // Use class name checks to avoid early loading of Fluids/FluidTags, which can cause deadlocks in Mixins.
        String className = fluid.getClass().getName();
        if (className.contains("WaterFluid") && FluidLanternManager.isWaterProtected(toPos)) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }
        if (className.contains("LavaFluid") && FluidLanternManager.isLavaProtected(toPos)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
