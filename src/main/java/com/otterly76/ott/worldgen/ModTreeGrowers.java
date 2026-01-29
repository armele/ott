package com.otterly76.ott.worldgen;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower OAK = new TreeGrower(
            "oak",
            Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.withDefaultNamespace("oak"))),
            Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.withDefaultNamespace("fancy_oak"))),
            Optional.empty()
    );

    public static final TreeGrower PALE_OAK = new TreeGrower(
            "pale_oak",
            Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.withDefaultNamespace("pale_oak"))),
            Optional.empty(),
            Optional.empty()
    );
}



