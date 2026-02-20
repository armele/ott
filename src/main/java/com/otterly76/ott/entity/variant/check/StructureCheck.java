package com.otterly76.ott.entity.variant.check;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.entity.variant.SpawnCondition;
import com.otterly76.ott.entity.variant.SpawnContext;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

public record StructureCheck(HolderSet<Structure> requiredStructures) implements SpawnCondition {
    public static final MapCodec<StructureCheck> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            RegistryCodecs.homogeneousList(Registries.STRUCTURE).fieldOf("structures").forGetter(StructureCheck::requiredStructures)
    ).apply(instance, StructureCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return StructureCheckHelper.getStructureWithPieceAt(context, this.requiredStructures::contains).isValid();
    }

    @Override
    public @NotNull MapCodec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}