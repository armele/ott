package com.otterly76.ott.entity.variant.check;

import com.otterly76.ott.entity.variant.SpawnContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.Optional;
import java.util.function.Predicate;

public final class StructureCheckHelper {
    private StructureCheckHelper() {}

    public static StructureStart getStructureWithPieceAt(SpawnContext context, Predicate<Holder<Structure>> predicate) {
        StructureManager manager = context.level().getLevel().structureManager();
        BlockPos pos = context.pos();
        Registry<Structure> registry = manager.registryAccess().registryOrThrow(Registries.STRUCTURE);

        for(StructureStart start : manager.startsForStructure(new ChunkPos(pos), (structure) -> {
            Optional<Holder.Reference<Structure>> holder = registry.getHolder(registry.getId(structure));
            return holder.map(predicate::test).orElse(false);
        })) {
            if (manager.structureHasPieceAt(pos, start)) {
                return start;
            }
        }

        return StructureStart.INVALID_START;
    }
}