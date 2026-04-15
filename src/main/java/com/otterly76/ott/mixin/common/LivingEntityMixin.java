package com.otterly76.ott.mixin.common;

import com.otterly76.ott.registry.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    /** Freeze the air supply when Breath of Nautilus is active. */
    @Inject(method = "decreaseAirSupply(I)I", at = @At("HEAD"), cancellable = true)
    private void ott$decreaseAirSupply(int currentAir, CallbackInfoReturnable<Integer> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self.hasEffect(ModEffects.BREATH_OF_NAUTILUS)) {
            cir.setReturnValue(currentAir);
        }
    }

    /** Horses/camels/donkeys don't sink when a player is riding them (Mounts of Mayhem). */
    @Inject(method = "isAffectedByFluids()Z", at = @At("HEAD"), cancellable = true)
    private void ott$isAffectedByFluids(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof AbstractHorse && self.hasControllingPassenger()) {
            cir.setReturnValue(false);
        }
    }
}
