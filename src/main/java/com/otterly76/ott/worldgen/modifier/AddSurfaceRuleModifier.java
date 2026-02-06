package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;

import java.util.List;

public record AddSurfaceRuleModifier(int priority, List<ResourceKey<LevelStem>> levels, SurfaceRules.RuleSource surfaceRule) implements Modifier {
    public static final MapCodec<AddSurfaceRuleModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_DEFAULT.forGetter(AddSurfaceRuleModifier::priority), ResourceKey.codec(Registries.LEVEL_STEM).listOf().fieldOf("levels").forGetter(AddSurfaceRuleModifier::levels), RuleSource.CODEC.fieldOf("surface_rule").forGetter(AddSurfaceRuleModifier::surfaceRule)).apply(instance, AddSurfaceRuleModifier::new));

    public void applyModifier() {
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}
