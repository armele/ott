package com.otterly76.ott.trim;

import com.otterly76.ott.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.Map;

public class ModTrimMaterials {
    public static final ResourceKey<TrimMaterial> RESIN;
    public static final ResourceKey<TrimMaterial> COPPER;

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, ModItems.RESIN_BRICK.get(), Style.EMPTY.withColor(TextColor.parseColor("#fc7812").getOrThrow()));
        register(context, COPPER, Items.COPPER_INGOT, Style.EMPTY.withColor(TextColor.parseColor("#B4684D").getOrThrow()), 0.5F, Map.of(com.otterly76.ott.registry.ModArmorMaterials.COPPER, "copper_darker"));
    }

    private static void register(BootstrapContext<TrimMaterial> context, Item item, Style style) {
        register(context, ModTrimMaterials.RESIN, item, style, (float) 0.113, Map.of());
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Item item, Style style, float itemModelIndex, Map<Holder<ArmorMaterial>, String> overrides) {
        TrimMaterial trimmaterial = TrimMaterial.create(trimKey.location().getPath(), item, itemModelIndex, Component.translatable(Util.makeDescriptionId("trim_material", trimKey.location())).withStyle(style), overrides);
        context.register(trimKey, trimmaterial);
    }

    static {
        RESIN = ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath("minecraft", "resin"));
        COPPER = ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath("minecraft", "copper"));
    }
}
