package com.otterly76.ott.worldgen;

import com.otterly76.ott.worldgen.placement.RiverLichenFilter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = registerKey("flower_pale_garden");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = registerKey("pale_garden_vegetation");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = registerKey("pale_garden_flowers");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = registerKey("pale_moss_patch");
    public static final ResourceKey<PlacedFeature> PALE_OAK_CHECKED = registerKey("pale_oak_checked");
    public static final ResourceKey<PlacedFeature> PALE_OAK_CREAKING_CHECKED = registerKey("pale_oak_creaking_checked");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        // Empty: Data is handled by manual JSON files in resources
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.withDefaultNamespace(name));
    }

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
            DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, "ott");

    public static final Supplier<PlacementModifierType<RiverLichenFilter>> RIVER_LICHEN_FILTER =
            PLACEMENT_MODIFIERS.register("river_lichen_filter", () -> RiverLichenFilter.TYPE);
}