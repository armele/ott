package com.otterly76.ott.worldgen;

import com.otterly76.ott.Constants;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    // Note: You might need to adjust the ConfiguredFeature keys to match your actual datagen keys
    public static final TreeGrower PALE_OAK = new TreeGrower(
            Constants.MOD_ID + ":pale_oak",
            Optional.empty(), // Mega Tree
            Optional.of(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "pale_oak"))),
            Optional.empty() // Flowers
    );
}