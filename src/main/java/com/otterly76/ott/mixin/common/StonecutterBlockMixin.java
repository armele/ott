package com.otterly76.ott.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class StonecutterBlockMixin {
    /**
     * The vanilla StonecutterBlock's inherited occlusion shape is Box(0,0,0,16,9,16)
     * whose bottom face is a full 16×16, causing the top face of the block placed
     * beneath it to be culled. Return an empty shape so no adjacent faces are culled.
     * Must target BlockBehavior (the defining class) since StonecutterBlock does not
     * override getOcclusionShape — Mixin cannot inject into inherited methods.
     */
    @Inject(method = "getOcclusionShape", at = @At("HEAD"), cancellable = true)
    private void ott$stonecutterEmptyOcclusion(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        if ((Object) this instanceof StonecutterBlock) {
            cir.setReturnValue(Shapes.empty());
        }
    }
}