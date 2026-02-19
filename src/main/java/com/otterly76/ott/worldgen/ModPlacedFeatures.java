package com.otterly76.ott.worldgen;

import com.otterly76.ott.Constants;
import com.otterly76.ott.worldgen.placement.RiverLichenFilter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> VERDANT_FOREST_AZALEA = registerKey(Constants.MOD_ID, "verdant_forest_azalea");
    public static final ResourceKey<PlacedFeature> GIANT_HOLLOW_ROOT_ARCH = registerKey(Constants.MOD_ID, "giant_hollow_root_arch");
    public static final ResourceKey<PlacedFeature> GIANT_HOLLOW_ROOT_SPIKE = registerKey(Constants.MOD_ID, "giant_hollow_root_spike");


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
    }

    private static ResourceKey<PlacedFeature> registerKey(String namespace, String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(namespace, name));
    }

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
            DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, "ott");

    public static final Supplier<PlacementModifierType<RiverLichenFilter>> RIVER_LICHEN_FILTER =
            PLACEMENT_MODIFIERS.register("river_lichen_filter", () -> RiverLichenFilter.TYPE);
}