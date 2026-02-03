package com.otterly76.ott.mixin.common.template.mansion;

import com.otterly76.ott.config.ConfigHandler;
import com.otterly76.ott.duck.RegistryHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

@Mixin(WoodlandMansionStructure.class)
public class WoodlandMansionStructureMixin {
    @Inject(
            method = "generatePieces",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ott$injectRegistriesForTemplateList(StructurePiecesBuilder builder, Structure.GenerationContext context, BlockPos pos, Rotation rotation, CallbackInfo ci) {
        if (ConfigHandler.getConfig().breaksSeedParity()) {
            // Use reflection/Duck interfaces to handle the non-public classes
            try {
                // We reference the pieces class to get access to the inner static classes
                // If ATs are failing, we use Object and casting
                Object grid = Class.forName("net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces$MansionGrid")
                        .getConstructor(RandomSource.class).newInstance(context.random());

                Object placer = Class.forName("net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces$MansionPiecePlacer")
                        .getConstructor(StructureTemplateManager.class, RandomSource.class)
                        .newInstance(context.structureTemplateManager(), context.random());

                // Use your duck interface!
                ((RegistryHolder)placer).ott$setRegistries(context.registryAccess());

                LinkedList<WoodlandMansionPieces.WoodlandMansionPiece> list = new LinkedList<>();

                // Invoke the method via reflection since it's not public
                placer.getClass().getMethod("createMansion", BlockPos.class, Rotation.class, List.class, grid.getClass())
                        .invoke(placer, pos, rotation, list, grid);

                list.forEach(builder::addPiece);
                ci.cancel();
            } catch (Exception e) {
                // If this fails, the mansion will generate normally (vanilla fallback)
            }
        }
    }
}