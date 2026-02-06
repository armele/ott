package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.mixin.common.StructureSetAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record RemoveStructureSetEntriesModifier(int priority, HolderSet<StructureSet> structureSets, List<Holder<Structure>> entries) implements Modifier {
    public static final MapCodec<RemoveStructureSetEntriesModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_REMOVE.forGetter(RemoveStructureSetEntriesModifier::priority), OttCodecs.registrySet(Registries.STRUCTURE_SET, "structure_sets").forGetter(RemoveStructureSetEntriesModifier::structureSets), Structure.CODEC.listOf().fieldOf("structures").forGetter(RemoveStructureSetEntriesModifier::entries)).apply(instance, RemoveStructureSetEntriesModifier::new));

    public void applyModifier() {
        this.structureSets.stream().map(Holder::value).forEach(this::applyModifier);
    }

    private void applyModifier(StructureSet structureSet) {
        // Use (Object) bridge to bypass the inconvertible types error
        StructureSetAccessor structureSetAccessor = (StructureSetAccessor) (Object) structureSet;
        List<StructureSet.StructureSelectionEntry> structureSelectionEntries = new ArrayList<>(structureSet.structures());
        structureSetAccessor.setStructures(structureSelectionEntries.stream()
                .filter((setEntry) -> !this.entries.contains(setEntry.structure()))
                .collect(Collectors.toList()));
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}
