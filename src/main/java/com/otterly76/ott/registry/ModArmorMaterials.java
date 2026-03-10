package com.otterly76.ott.registry;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, "minecraft");

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COPPER = ARMOR_MATERIALS.register("copper", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("copper"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EXPOSED_COPPER = ARMOR_MATERIALS.register("exposed_copper", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("exposed_copper"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WEATHERED_COPPER = ARMOR_MATERIALS.register("weathered_copper", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("weathered_copper"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OXIDIZED_COPPER = ARMOR_MATERIALS.register("oxidized_copper", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("oxidized_copper"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COPPER_CHAINMAIL = ARMOR_MATERIALS.register("copper_chainmail", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }),
            12,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("copper_chainmail"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EXPOSED_COPPER_CHAINMAIL = ARMOR_MATERIALS.register("exposed_copper_chainmail", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }),
            12,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("exposed_copper_chainmail"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WEATHERED_COPPER_CHAINMAIL = ARMOR_MATERIALS.register("weathered_copper_chainmail", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }),
            12,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("weathered_copper_chainmail"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OXIDIZED_COPPER_CHAINMAIL = ARMOR_MATERIALS.register("oxidized_copper_chainmail", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 4);
            }),
            12,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("oxidized_copper_chainmail"))),
            0.0F,
            0.0F
    ));

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}