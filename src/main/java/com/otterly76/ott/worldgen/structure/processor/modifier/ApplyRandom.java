package com.otterly76.ott.worldgen.structure.processor.modifier;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.util.weighted.WeightedList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ApplyRandom(WeightedList<RuleBlockEntityModifier> modifiers) implements RuleBlockEntityModifier {
    public static final MapCodec<ApplyRandom> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(WeightedList.codec(RuleBlockEntityModifier.CODEC).fieldOf("modifiers").forGetter(ApplyRandom::modifiers)).apply(instance, ApplyRandom::new));
    public static final RuleBlockEntityModifierType<ApplyRandom> TYPE = () -> CODEC;

    public @Nullable CompoundTag apply(@NotNull RandomSource randomSource, @Nullable CompoundTag compoundTag) {
        Optional<RuleBlockEntityModifier> modifier = this.modifiers.getRandom(randomSource);
        return modifier.isPresent() ? modifier.get().apply(randomSource, compoundTag) : compoundTag;
    }

    public @NotNull RuleBlockEntityModifierType<?> getType() {
        return TYPE;
    }
}

