package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.mixin.common.StructureSetAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Modifier} that appends new {@link StructureSet.StructureSelectionEntry} items
 * to an existing {@link StructureSet}, mirroring Lithostitched's
 * {@code lithostitched:add_structure_set_entries} modifier.
 */
public record AddStructureSetEntriesModifier(
        HolderSet<StructureSet> structureSets,
        List<StructureSet.StructureSelectionEntry> entries
) implements Modifier {

    public static final MapCodec<AddStructureSetEntriesModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET).fieldOf("structure_sets").forGetter(AddStructureSetEntriesModifier::structureSets),
            StructureSet.StructureSelectionEntry.CODEC.listOf().fieldOf("entries").forGetter(AddStructureSetEntriesModifier::entries)
    ).apply(instance, AddStructureSetEntriesModifier::new));

    @Override
    public void applyModifier() {
        this.structureSets.stream().map(Holder::value).forEach(this::addEntries);
    }

    private void addEntries(StructureSet structureSet) {
        StructureSetAccessor accessor = (StructureSetAccessor) (Object) structureSet;
        List<StructureSet.StructureSelectionEntry> combined = new ArrayList<>(structureSet.structures());
        combined.addAll(this.entries);
        accessor.setStructures(combined);
    }

    @Override
    public int priority() {
        return 1000;
    }

    @Override
    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}
