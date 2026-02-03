package com.otterly76.ott.mixin.common.processor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.otterly76.ott.config.ConfigHandler;
import com.otterly76.ott.worldgen.processor.UnboundReferenceProcessor;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalPiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({RuinedPortalPiece.class})
public class RuinedPortalPieceMixin {
    @ModifyReturnValue(
            method = {"makeSettings(Lnet/minecraft/world/level/block/Mirror;Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$VerticalPlacement;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;"},
            at = {@At("RETURN")}
    )
    private static StructurePlaceSettings addShipwreckProcessor(StructurePlaceSettings settings) {
        return ConfigHandler.getConfig().breaksSeedParity() ? settings.addProcessor(UnboundReferenceProcessor.of("ruined_portal")) : settings;
    }
}