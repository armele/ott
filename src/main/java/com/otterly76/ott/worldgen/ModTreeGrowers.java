package com.otterly76.ott.worldgen;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    // Backport note: these configured features live in the "minecraft" namespace on purpose.
    public static final TreeGrower PALE_OAK = new TreeGrower(
            "minecraft:pale_oak",
            Optional.of(net.minecraft.resources.ResourceKey.create(
                    net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("minecraft", "pale_oak_mega")
            )), // Mega Tree (2x2 trunk variant) - only used if you register it
            Optional.of(net.minecraft.resources.ResourceKey.create(
                    net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("minecraft", "pale_oak")
            )),
            Optional.empty() // Flowers
    );
}