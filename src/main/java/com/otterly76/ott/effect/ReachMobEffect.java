package com.otterly76.ott.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ReachMobEffect extends MobEffect {

    public ReachMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(
                Attributes.BLOCK_INTERACTION_RANGE,
                ResourceLocation.fromNamespaceAndPath("ott", "effect.reach"),
                1.5,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

}
