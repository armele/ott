package com.otterly76.ott.mixin.common;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin {
    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean ott$silenceVanillaDeathMessage(GameRules instance, GameRules.Key<GameRules.BooleanValue> key) {
        if (key == GameRules.RULE_SHOWDEATHMESSAGES) {
            return false;
        }
        return instance.getBoolean(key);
    }
}
