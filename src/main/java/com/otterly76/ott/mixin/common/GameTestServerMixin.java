package com.otterly76.ott.mixin.common;


import com.otterly76.ott.worldgen.modifier.ModifierManager;
import com.otterly76.ott.worldgen.surface.SurfaceRuleManager;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameTestServer.class})
public abstract class GameTestServerMixin {
    @Inject(
            method = "initServer()Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/gametest/framework/GameTestServer;loadLevel()V"
            )
    )
    private void ott$initServer(CallbackInfoReturnable<Boolean> info) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        ModifierManager.applyModifiers(server);
        SurfaceRuleManager.applySurfaceRules(server);
    }
}
