package com.otterly76.ott.worldgen;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower PALE_OAK = new TreeGrower(
            "minecraft:pale_oak",
            Optional.of(net.minecraft.resources.ResourceKey.create(
                    net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("minecraft", "pale_oak")
            )),
            Optional.of(net.minecraft.resources.ResourceKey.create(
                    net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("minecraft", "pale_oak_mega")
            )),

            Optional.empty()
    );
}