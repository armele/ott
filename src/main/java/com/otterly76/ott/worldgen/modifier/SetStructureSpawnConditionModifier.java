package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.Ott;
import com.otterly76.ott.mixin.common.HolderReferenceAccessor;
import com.otterly76.ott.mixin.common.MappedRegistryAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import com.otterly76.ott.worldgen.placement.condition.PlacementCondition;
import com.otterly76.ott.worldgen.structure.DelegatingConfig;
import com.otterly76.ott.worldgen.structure.DelegatingStructure;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;

public record SetStructureSpawnConditionModifier(int priority, HolderSet<Structure> structures, PlacementCondition spawnCondition, boolean append) implements Modifier {
    public static final MapCodec<SetStructureSpawnConditionModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_DEFAULT.forGetter(SetStructureSpawnConditionModifier::priority), OttCodecs.registrySet(Registries.STRUCTURE, "structures").forGetter(SetStructureSpawnConditionModifier::structures), PlacementCondition.CODEC.fieldOf("spawn_condition").forGetter(SetStructureSpawnConditionModifier::spawnCondition), Codec.BOOL.fieldOf("append").orElse(true).forGetter(SetStructureSpawnConditionModifier::append)).apply(instance, SetStructureSpawnConditionModifier::new));

    public void applyModifier(RegistryAccess registries) {
        this.structures.forEach((structure) -> this.applyModifier(registries, structure));
    }

    private void applyModifier(RegistryAccess registries, Holder<Structure> structure) {
        Object value = structure.value();
        if (value instanceof DelegatingStructure delegating) {
            delegating.config().setSpawnCondition(this.spawnCondition, this.append);
        } else if (structure instanceof Holder.Reference<Structure> reference) {
            Structure delegating = new DelegatingStructure(new DelegatingConfig(Holder.direct(structure.value()), this.spawnCondition));

            // Suppress the unavoidable unchecked warnings for the Mixin Accessor casts
            @SuppressWarnings("unchecked")
            HolderReferenceAccessor<Structure> holderAccessor = (HolderReferenceAccessor<Structure>) structure;
            holderAccessor.setValue(delegating);

            @SuppressWarnings("unchecked")
            MappedRegistryAccessor<Structure> registryAccessor = (MappedRegistryAccessor<Structure>) Ott.registry(registries, Registries.STRUCTURE);
            registryAccessor.getByValue().put(delegating, reference);
        }
    }

    public void applyModifier() {
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}




