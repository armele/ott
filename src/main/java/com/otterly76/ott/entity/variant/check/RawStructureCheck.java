package com.otterly76.ott.entity.variant.check;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.entity.variant.SpawnCondition;
import com.otterly76.ott.entity.variant.SpawnContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public record RawStructureCheck(TagKey<Structure> requiredStructures) implements SpawnCondition {
    public static final MapCodec<RawStructureCheck> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            TagKey.codec(Registries.STRUCTURE).fieldOf("structures").forGetter(RawStructureCheck::requiredStructures)
    ).apply(instance, RawStructureCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return this.getStructureWithPieceAt(context, (holder) -> holder.is(this.requiredStructures)).isValid();
    }

    private StructureStart getStructureWithPieceAt(SpawnContext context, Predicate<Holder<Structure>> predicate) {
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

    @Override
    public @NotNull MapCodec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
