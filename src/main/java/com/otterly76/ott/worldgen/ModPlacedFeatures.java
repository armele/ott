package com.otterly76.ott.worldgen;

import com.otterly76.ott.Constants;
import com.otterly76.ott.worldgen.placement.RiverLichenFilter;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> LUSH_GLADE_AZALEA = registerKey("lush_glade_azalea");
    public static final ResourceKey<PlacedFeature> GIANT_HOLLOW_ROOT_ARCH = registerKey("giant_hollow_root_arch");
    public static final ResourceKey<PlacedFeature> GIANT_HOLLOW_ROOT_SPIKE = registerKey("giant_hollow_root_spike");

    public static final ResourceKey<PlacedFeature> OAK_NESTED_OAK_TREE_CHECKED = registerKey("oak_nested_oak");
    public static final ResourceKey<PlacedFeature> OAK_NESTED_OAK_TREES = registerKey("oak_nested_oak_trees");


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context, OAK_NESTED_OAK_TREE_CHECKED, configuredFeatures.getOrThrow(ModConfiguredFeatures.OAK_NESTED_OAK),
                List.of(PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)));

        PlacementUtils.register(context, OAK_NESTED_OAK_TREES, configuredFeatures.getOrThrow(ModConfiguredFeatures.OAK_NESTED_OAK_TREE_FILTERED),
                VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(75)));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
            DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, "ott");

    public static final Supplier<PlacementModifierType<RiverLichenFilter>> RIVER_LICHEN_FILTER =
            PLACEMENT_MODIFIERS.register("river_lichen_filter", () -> RiverLichenFilter.TYPE);
}
