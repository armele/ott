package com.otterly76.ott.entity.variant.check;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.entity.variant.SpawnCondition;
import com.otterly76.ott.entity.variant.SpawnContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

public record RawStructureCheck(TagKey<Structure> requiredStructures) implements SpawnCondition {
    public static final MapCodec<RawStructureCheck> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            TagKey.codec(Registries.STRUCTURE).fieldOf("structures").forGetter(RawStructureCheck::requiredStructures)
    ).apply(instance, RawStructureCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return StructureCheckHelper.getStructureWithPieceAt(context, (holder) -> holder.is(this.requiredStructures)).isValid();
    }

    @Override
    public @NotNull MapCodec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}