package com.otterly76.ott.mixin.common;

import com.otterly76.ott.Ott;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FarmBlock.class})
public abstract class MixinFarmBlock {
    @Inject(
            at = {@At("RETURN")},
            method = {"canSurvive"},
            cancellable = true
    )
    public void canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos, CallbackInfoReturnable<Boolean> cir) {
        if (!(Boolean)cir.getReturnValue()) {
            BlockState blockstate = pLevel.getBlockState(pPos.above());
            cir.setReturnValue(blockstate.is(Ott.FARMLAND_CANSURVIVE));
        }
    }
}