package com.otterly76.ott.mixin.common;

import com.otterly76.ott.handler.WeatheringHandler;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HoneycombItem.class)
public class HoneycombItemMixin {
    @Inject(method = "getWaxed(Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private static void ott$getWaxed(BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
        WeatheringHandler.getWaxed(state.getBlock()).ifPresent(waxed -> cir.setReturnValue(Optional.of(waxed.withPropertiesOf(state))));
    }
}