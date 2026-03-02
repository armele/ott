package com.otterly76.ott.registry;

import com.otterly76.ott.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.Map;

public class ModTrimMaterials {
    public static final ResourceKey<TrimMaterial> RESIN = registryKey();

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, ModItems.RESIN_BRICK.get(), Style.EMPTY.withColor(16545810));
    }

    private static void register(BootstrapContext<TrimMaterial> context, Item ingredient, Style style) {
        register(context, ingredient, style, Map.of());
    }

    private static void register(BootstrapContext<TrimMaterial> context, Item ingredient, Style style, Map<Holder<ArmorMaterial>, String> overrideArmorMaterials) {
        TrimMaterial trimMaterial = TrimMaterial.create(ModTrimMaterials.RESIN.location().getPath(), ingredient, (float) 0.5, Component.translatable(Util.makeDescriptionId("trim_material", ModTrimMaterials.RESIN.location())).withStyle(style), overrideArmorMaterials);
        context.register(ModTrimMaterials.RESIN, trimMaterial);
    }

    private static ResourceKey<TrimMaterial> registryKey() {
        return ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath("minecraft", "resin"));
    }
}
