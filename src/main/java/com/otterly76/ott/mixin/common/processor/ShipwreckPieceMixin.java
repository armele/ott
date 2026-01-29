package com.otterly76.ott.mixin.common.processor;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.otterly76.ott.neoforge.impl.config.ConfigHandler;
import com.otterly76.ott.worldgen.structure.processor.UnboundReferenceProcessor;
import net.minecraft.world.level.levelgen.structure.structures.ShipwreckPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ShipwreckPieces.ShipwreckPiece.class})
public class ShipwreckPieceMixin {
    @ModifyReturnValue(
            method = {"makeSettings(Lnet/minecraft/world/level/block/Rotation;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;"},
            at = {@At("RETURN")}
    )
    private static StructurePlaceSettings addShipwreckProcessor(StructurePlaceSettings settings) {
        return ConfigHandler.getConfig().breaksSeedParity() ? settings.addProcessor(UnboundReferenceProcessor.of("shipwreck")) : settings;
    }
}

