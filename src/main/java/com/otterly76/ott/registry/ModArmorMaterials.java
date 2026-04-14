package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
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
    public static final DeferredRegister<ArmorMaterial> OTT_ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Constants.MOD_ID);

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

    // ── Netherite Horse Armor (minecraft namespace) ───────────────────────────

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NETHERITE_HORSE_ARMOR = ARMOR_MATERIALS.register("netherite_horse_armor", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
                map.put(ArmorItem.Type.BODY, 19);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(Items.NETHERITE_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("netherite"))),
            3.0F,
            0.1F
    ));

    // ── Nautilus armor (minecraft namespace) ─────────────────────────────────

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COPPER_NAUTILUS_ARMOR = ARMOR_MATERIALS.register("copper_nautilus_armor", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
                map.put(ArmorItem.Type.BODY, 3);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("copper_nautilus_armor"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> IRON_NAUTILUS_ARMOR = ARMOR_MATERIALS.register("iron_nautilus_armor", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
                map.put(ArmorItem.Type.BODY, 5);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(Items.IRON_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("iron_nautilus_armor"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GOLDEN_NAUTILUS_ARMOR = ARMOR_MATERIALS.register("golden_nautilus_armor", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
                map.put(ArmorItem.Type.BODY, 7);
            }),
            25,
            SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(Items.GOLD_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("golden_nautilus_armor"))),
            0.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> DIAMOND_NAUTILUS_ARMOR = ARMOR_MATERIALS.register("diamond_nautilus_armor", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
                map.put(ArmorItem.Type.BODY, 11);
            }),
            10,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            () -> Ingredient.of(Items.DIAMOND),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("diamond_nautilus_armor"))),
            2.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NETHERITE_NAUTILUS_ARMOR = ARMOR_MATERIALS.register("netherite_nautilus_armor", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
                map.put(ArmorItem.Type.BODY, 13);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(Items.NETHERITE_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("netherite_nautilus_armor"))),
            3.0F,
            0.1F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WILDFIRE_CROWN = OTT_ARMOR_MATERIALS.register("wildfire_crown", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 6);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(Items.BLAZE_ROD),
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath("ott", "wildfire_crown"))),
            1.0F,
            0.0F
    ));

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
        OTT_ARMOR_MATERIALS.register(eventBus);
    }
}
