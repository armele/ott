package com.otterly76.ott.mixin.common.processor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.otterly76.ott.config.ConfigHandler;
import com.otterly76.ott.worldgen.processor.UnboundReferenceProcessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinPieces;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({OceanRuinPieces.OceanRuinPiece.class})
public class OceanRuinPieceMixin {
    @ModifyReturnValue(
            method = {"makeSettings(Lnet/minecraft/world/level/block/Rotation;FLnet/minecraft/world/level/levelgen/structure/structures/OceanRuinStructure$Type;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;"},
            at = {@At("RETURN")}
    )
    private static StructurePlaceSettings addShipwreckProcessor(StructurePlaceSettings settings, Rotation rotation, float f, OceanRuinStructure.Type type) {
        return ConfigHandler.getConfig().breaksSeedParity() ? settings.addProcessor(UnboundReferenceProcessor.of("ocean_ruin_" + type.getName())) : settings;
    }
}
