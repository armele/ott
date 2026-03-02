package com.otterly76.ott.entity.variant.check;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.entity.variant.SpawnCondition;
import com.otterly76.ott.entity.variant.SpawnContext;
import net.minecraft.advancements.critereon.MinMaxBounds;
import org.jetbrains.annotations.NotNull;

public record MoonBrightnessCheck(MinMaxBounds.Doubles range) implements SpawnCondition {
    public static final MapCodec<MoonBrightnessCheck> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            MinMaxBounds.Doubles.CODEC.fieldOf("range").forGetter(MoonBrightnessCheck::range)
    ).apply(instance, MoonBrightnessCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return this.range.matches(context.level().getLevel().getMoonBrightness());
    }

    @Override
    public @NotNull MapCodec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
