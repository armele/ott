package com.otterly76.ott.mixin.server;


import com.otterly76.ott.worldgen.modifier.ModifierManager;
import com.otterly76.ott.worldgen.surface.SurfaceRuleManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DedicatedServer.class})
public final class DedicatedServerMixin {
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
