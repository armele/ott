package com.otterly76.ott.mixin.common.processor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.otterly76.ott.config.ConfigHandler;
import com.otterly76.ott.worldgen.processor.UnboundReferenceProcessor;
import net.minecraft.world.level.levelgen.structure.structures.NetherFossilPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({NetherFossilPieces.NetherFossilPiece.class})
public class NetherFossilPieceMixin {
    @ModifyReturnValue(
            method = {"makeSettings(Lnet/minecraft/world/level/block/Rotation;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;"},
            at = {@At("RETURN")}
    )
    private static StructurePlaceSettings addShipwreckProcessor(StructurePlaceSettings settings) {
        return ConfigHandler.getConfig().breaksSeedParity() ? settings.addProcessor(UnboundReferenceProcessor.of("nether_fossil")) : settings;
    }
}
