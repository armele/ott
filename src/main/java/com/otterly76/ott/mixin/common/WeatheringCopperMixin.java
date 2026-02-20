package com.otterly76.ott.mixin.common;

import com.otterly76.ott.handler.WeatheringHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WeatheringCopper.class)
public interface WeatheringCopperMixin {
    @Inject(method = "getNext(Lnet/minecraft/world/level/block/Block;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private static void ott$getNext(Block block, CallbackInfoReturnable<Optional<Block>> cir) {
        WeatheringHandler.getNext(block).ifPresent(next -> cir.setReturnValue(Optional.of(next)));
    }

    @Inject(method = "getPrevious(Lnet/minecraft/world/level/block/Block;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private static void ott$getPrevious(Block block, CallbackInfoReturnable<Optional<Block>> cir) {
        WeatheringHandler.getPrevious(block).ifPresent(prev -> cir.setReturnValue(Optional.of(prev)));
    }
}