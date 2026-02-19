package com.otterly76.ott.mixin.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin extends MobMixin {
    @Shadow public abstract boolean isTame();

    protected TamableAnimalMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean ott$silenceVanillaDeathMessage(GameRules instance, GameRules.Key<GameRules.BooleanValue> key) {
        if (key == GameRules.RULE_SHOWDEATHMESSAGES) {
            return false;
        }
        return instance.getBoolean(key);
    }
}
