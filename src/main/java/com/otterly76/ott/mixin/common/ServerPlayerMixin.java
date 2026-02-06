package com.otterly76.ott.mixin.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean ott$silenceVanillaDeathMessage(GameRules instance, GameRules.Key<GameRules.BooleanValue> key) {
        if (key == GameRules.RULE_SHOWDEATHMESSAGES) {
            return false;
        }
        return instance.getBoolean(key);
    }
}
