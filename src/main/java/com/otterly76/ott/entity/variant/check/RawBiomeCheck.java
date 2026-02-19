package com.otterly76.ott.entity.variant.check;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.entity.variant.SpawnCondition;
import com.otterly76.ott.entity.variant.SpawnContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

public record RawBiomeCheck(TagKey<Biome> requiredBiomes) implements SpawnCondition {
    public static final MapCodec<RawBiomeCheck> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            TagKey.codec(Registries.BIOME).fieldOf("biomes").forGetter(RawBiomeCheck::requiredBiomes)
    ).apply(instance, RawBiomeCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return context.biome().is(this.requiredBiomes);
    }

    @Override
    public @NotNull MapCodec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
