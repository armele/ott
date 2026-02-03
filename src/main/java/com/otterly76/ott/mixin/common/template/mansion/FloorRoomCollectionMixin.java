package com.otterly76.ott.mixin.common.template.mansion;

import com.otterly76.ott.duck.RegistryHolder;
import net.minecraft.core.RegistryAccess;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces$FloorRoomCollection")
public abstract class FloorRoomCollectionMixin implements RegistryHolder {
    @Unique
    private RegistryAccess ott$registries;

    @Override
    public RegistryAccess ott$getRegistries() {
        return this.ott$registries;
    }

    @Override
    public void ott$setRegistries(RegistryAccess registries) {
        this.ott$registries = registries;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void ott$onInit(CallbackInfo ci) {
        // NeoForge provides a global hook to the current server instance.
        // This retrieves the registries directly, bypassing the need for PiecePlacerMixin's private ThreadLocal.
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            this.ott$setRegistries(ServerLifecycleHooks.getCurrentServer().registryAccess());
        }
    }
}