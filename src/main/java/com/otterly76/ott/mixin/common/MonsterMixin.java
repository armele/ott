package com.otterly76.ott.mixin.common;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public class MonsterMixin {
    @Inject(method = "isPreventingPlayerRest", at = @At(value = "HEAD"), cancellable = true)
    private void ott$onIsPreventingPlayerRest(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player.isHolding(ModBlocks.TEDDY_BEAR.get().asItem())) {
            cir.setReturnValue(false);
        }
    }
}