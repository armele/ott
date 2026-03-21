package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.loot.AddItemModifier;
import com.otterly76.ott.loot.RemoveItemsModifier;
import com.otterly76.ott.util.ModTags;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    protected void start() {
        add("remove_skeleton_drops", new RemoveItemsModifier(new LootItemCondition[] {
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModTags.EntityTypes.SKELETON_MOBS))).build()
        }, List.of(Items.BOW, Items.STONE_SWORD)));

        add("copper_sword", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.BASTION_OTHER.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE.location())
                ).build()
        }, ModItems.COPPER_SWORD.get(), 1));

        add("copper_pickaxe", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_TOOLSMITH.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CROSSING.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT.location())
                ).build()
        }, ModItems.COPPER_PICKAXE.get(), 1));

        add("copper_boots", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_ARMORER.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR.location())
                ).build()
        }, ModItems.COPPER_BOOTS.get(), 1));

        add("copper_chestplate", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_ARMORER.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR.location())
                ).build()
        }, ModItems.COPPER_CHESTPLATE.get(), 1));

        add("copper_helmet", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_ARMORER.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR.location())
                ).build()
        }, ModItems.COPPER_HELMET.get(), 1));

        add("copper_leggings", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_ARMORER.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR.location())
                ).build()
        }, ModItems.COPPER_LEGGINGS.get(), 1));

        add("copper_shovel", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_TOOLSMITH.location())
                ).build()
        }, ModItems.COPPER_SHOVEL.get(), 1));

        add("copper_rail", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT.location())
                ).build()
        }, ModBlocks.COPPER_RAILS.get("").get().asItem(), 8));

        add("copper_horse_armor", new AddItemModifier(new LootItemCondition[] {
                AnyOfCondition.anyOf(
                        LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.NETHER_BRIDGE.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.JUNGLE_TEMPLE.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.DESERT_PYRAMID.location()),
                        LootTableIdCondition.builder(BuiltInLootTables.END_CITY_TREASURE.location())
                ).build()
        }, ModItems.COPPER_HORSE_ARMOR.get(), 1));
    }
}
