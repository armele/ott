package com.otterly76.ott.mixin.common;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public abstract class WalkNodeEvaluatorMixin {

    @Inject(method = "getPathTypeOfMob", at = @At("HEAD"), cancellable = true)
    private void ott$animalsTreatOpenFenceGatesAsBlocked(PathfindingContext context,
                                                         int x, int y, int z,
                                                         Mob mob,
                                                         CallbackInfoReturnable<PathType> cir) {
        if (!(mob instanceof Animal)) return;

        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = context.getBlockState(pos);

        if (state.getBlock() instanceof FenceGateBlock && state.getValue(FenceGateBlock.OPEN)) {
            // Animals shouldn't consider open gates as a valid path -> no clustering/jitter.
            cir.setReturnValue(PathType.BLOCKED);
        }
    }
}





