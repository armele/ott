package com.otterly76.ott.mixin.common;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RuinedPortalStructure.class)
public class RuinedPortalStructureMixin {
    @Inject(method = "findGenerationPoint", at = @At("HEAD"), cancellable = true)
    private void ott$cancelRuinedPortal(Structure.GenerationContext context, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir) {
        if (!OttConfig.WORLDGEN.SPAWN_RUINED_PORTALS.get()) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
