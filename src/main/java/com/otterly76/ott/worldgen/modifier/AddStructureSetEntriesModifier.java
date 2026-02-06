package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.mixin.common.StructureSetAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry;

import java.util.ArrayList;
import java.util.List;

public record AddStructureSetEntriesModifier(int priority, HolderSet<StructureSet> structureSets, List<StructureSet.StructureSelectionEntry> entries) implements Modifier {
    public static final MapCodec<AddStructureSetEntriesModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_DEFAULT.forGetter(AddStructureSetEntriesModifier::priority), OttCodecs.registrySet(Registries.STRUCTURE_SET, "structure_sets").forGetter(AddStructureSetEntriesModifier::structureSets), StructureSelectionEntry.CODEC.listOf().fieldOf("entries").forGetter(AddStructureSetEntriesModifier::entries)).apply(instance, AddStructureSetEntriesModifier::new));

    public void applyModifier() {
        this.structureSets.stream().map(Holder::value).forEach(this::applyModifier);
    }

    public void applyModifier(StructureSet structureSet) {
        // Use (Object) bridge to bypass the inconvertible types error
        StructureSetAccessor structureSetAccessor = (StructureSetAccessor) (Object) structureSet;
        List<StructureSet.StructureSelectionEntry> structureSelectionEntries = new ArrayList<>(structureSet.structures());
        structureSelectionEntries.addAll(this.entries());
        structureSetAccessor.setStructures(structureSelectionEntries);
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}
