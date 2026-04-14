package com.otterly76.ott.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {

    /** Spear-exclusive enchantment that propels the player horizontally on a jab attack. */
    public static final ResourceKey<Enchantment> LUNGE = ResourceKey.create(
            Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace("lunge")
    );

    private ModEnchantments() {}
}
