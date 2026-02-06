package com.otterly76.ott.mixin.common.template.mansion;

import com.otterly76.ott.duck.RegistryHolder;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces$MansionPiecePlacer")
public class PiecePlacerMixin implements RegistryHolder {
    @Unique
    private static final ThreadLocal<RegistryAccess> OTT$REGISTRY_CONTEXT = new ThreadLocal<>();

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

    @Inject(method = "createMansion", at = @At("HEAD"))
    private void ott$pushRegistries(CallbackInfo ci) {
        OTT$REGISTRY_CONTEXT.set(this.ott$registries);
    }

    @Inject(method = "createMansion", at = @At("RETURN"))
    private void ott$popRegistries(CallbackInfo ci) {
        OTT$REGISTRY_CONTEXT.remove();
    }
}
