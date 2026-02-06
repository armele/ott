package com.otterly76.ott.mixin.client;

import com.otterly76.ott.worldgen.modifier.ModifierManager;
import com.otterly76.ott.worldgen.surface.SurfaceRuleManager;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({IntegratedServer.class})
public final class IntegratedServerMixin {
    @Inject(
            method = "initServer",
            at = @At("TAIL")
    )
    private void initServer(CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue()) {
            MinecraftServer server = (MinecraftServer) (Object) this;
            ModifierManager.applyModifiers(server);
            SurfaceRuleManager.applySurfaceRules(server);
        }
    }
}
