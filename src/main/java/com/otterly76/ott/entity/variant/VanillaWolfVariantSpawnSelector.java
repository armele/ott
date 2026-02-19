package com.otterly76.ott.entity.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.entity.animal.WolfVariant;

import java.util.List;

public record VanillaWolfVariantSpawnSelector(Holder<WolfVariant> vanillaVariant, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<VanillaWolfVariantSpawnSelector> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            RegistryFileCodec.create(Registries.WOLF_VARIANT, WolfVariant.DIRECT_CODEC).fieldOf("vanilla_variant").forGetter(VanillaWolfVariantSpawnSelector::vanillaVariant),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(VanillaWolfVariantSpawnSelector::spawnConditions)
    ).apply(instance, VanillaWolfVariantSpawnSelector::new));

    public static VanillaWolfVariantSpawnSelector fallback(Holder<WolfVariant> variant, int priority) {
        return new VanillaWolfVariantSpawnSelector(variant, SpawnPrioritySelectors.fallback(priority));
    }

    public static VanillaWolfVariantSpawnSelector withCondition(Holder<WolfVariant> variant, SpawnCondition condition, int priority) {
        return new VanillaWolfVariantSpawnSelector(variant, SpawnPrioritySelectors.single(condition, priority));
    }

    @Override
    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }
}
