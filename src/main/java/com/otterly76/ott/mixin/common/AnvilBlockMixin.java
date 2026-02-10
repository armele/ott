package com.otterly76.ott.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.otterly76.ott.handler.BlockConversionHandler;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(
        value = {AnvilBlock.class},
        priority = 2000
)
abstract class AnvilBlockMixin extends FallingBlock {
    public AnvilBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @ModifyVariable(
            method = {"damage"},
            at = @At("HEAD"),
            argsOnly = true)
    private static BlockState damage$0(BlockState blockState) {
        return BlockConversionHandler.convertToVanillaBlock(blockState);
    }

    @ModifyReturnValue(
            method = {"damage"},
            at = {@At("RETURN")}
    )
    private static BlockState damage$1(BlockState blockState) {
        return BlockConversionHandler.convertFromVanillaBlock(blockState);
    }
}