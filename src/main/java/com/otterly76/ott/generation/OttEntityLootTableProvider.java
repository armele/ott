package com.otterly76.ott.generation;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.item.ModItems;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class OttEntityLootTableProvider extends EntityLootSubProvider {
    public OttEntityLootTableProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        this.add(ModEntities.DUCK.get(), createChickenLikeDrops());
        this.add(ModEntities.GOOSE.get(), createChickenLikeDrops());
        this.add(ModEntities.BLUEJAY.get(), createBirdDrops());
        this.add(ModEntities.CANARY.get(), createBirdDrops());
        this.add(ModEntities.CARDINAL.get(), createBirdDrops());
        this.add(ModEntities.FINCH.get(), createBirdDrops());
        this.add(ModEntities.ROBIN.get(), createBirdDrops());
        this.add(ModEntities.SPARROW.get(), createBirdDrops());
        this.add(ModEntities.BROWN_BEAR.get(), createBearDrops());
        this.add(ModEntities.BLACK_BEAR.get(), createBearDrops());
        this.add(ModEntities.DEER.get(), createDeerDrops());
        this.add(ModEntities.REINDEER.get(), createDeerDrops());
        this.add(ModEntities.WHITE_DEER.get(), createDeerDrops());

        this.add(ModEntities.CAPYBARA.get(), createMeatDrops(ModItems.RAW_CAPYBARA.get()));
        this.add(ModEntities.HEDGEHOG.get(), createHedgehogDrops());
        this.add(ModEntities.KIWI.get(), createMeatDrops(ModItems.RAW_KIWI.get()));
        this.add(ModEntities.PENGUIN.get(), LootTable.lootTable());
        this.add(ModEntities.SEAL.get(), createMeatDrops(ModItems.RAW_SEAL.get()));
        this.add(ModEntities.JELLYFISH.get(), createJellyfishDrops());
        this.add(ModEntities.JELLYFISH_2.get(), createJellyfishDrops());
        this.add(ModEntities.JELLYFISH_3.get(), createJellyfishDrops());
        this.add(ModEntities.SEA_URCHIN.get(), createSeaUrchinDrops());
        this.add(ModEntities.SHRIMP_1.get(), createMeatDrops(ModItems.RAW_SHRIMP_1.get()));
        this.add(ModEntities.SEAHORSE_1.get(), LootTable.lootTable());
        this.add(ModEntities.STARFISH_1.get(), LootTable.lootTable());

        this.add(ModEntities.DRAGONFLY.get(), createDragonflyDrops());
        this.add(ModEntities.DUMBO_OCTOPUS.get(), LootTable.lootTable());
        this.add(ModEntities.FERRET.get(), LootTable.lootTable());
        this.add(ModEntities.JUMPING_SPIDER.get(), createJumpingSpiderDrops());
        this.add(ModEntities.KOI_FISH.get(), createKoiFishDrops());
        this.add(ModEntities.OTTER.get(), LootTable.lootTable());
        this.add(ModEntities.RED_PANDA.get(), LootTable.lootTable());
        this.add(ModEntities.SEA_BUNNY.get(), LootTable.lootTable());
        this.add(ModEntities.FIREFLY.get(), createFireflyDrops());
        this.add(ModEntities.SMALL_FIREFLY.get(), createFireflyDrops());
    }

    private LootTable.Builder createFireflyDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.GLOW_GOOP.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))));
    }

    private LootTable.Builder createDragonflyDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.DRAGONFLY_WING.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))));
    }

    private LootTable.Builder createJumpingSpiderDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SILK.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))));
    }

    private LootTable.Builder createKoiFishDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.KOI_FISH.get())))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.BONE_MEAL))
                        .when(net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance(0.05F)));
    }

    private LootTable.Builder createMeatDrops(net.minecraft.world.item.Item meat) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(meat)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)))))));
    }

    private LootTable.Builder createHedgehogDrops() {
        return createMeatDrops(ModItems.RAW_HEDGEHOG.get())
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.QUILL.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                                .when(net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition.killedByPlayer()))
                );
    }

    private LootTable.Builder createJellyfishDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.JELLYFISH_JELLY.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))));
    }

    private LootTable.Builder createSeaUrchinDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SEA_URCHIN_CAVIAR.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))));
    }

    private LootTable.Builder createBirdDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))));
    }

    private LootTable.Builder createChickenLikeDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.CHICKEN)
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)))))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))));
    }

    private LootTable.Builder createBearDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.FUR.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))));
    }

    private LootTable.Builder createDeerDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.VENISON.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)))))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.ANTLER.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))));
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                ModEntities.DUCK.get(),
                ModEntities.GOOSE.get(),
                ModEntities.BLUEJAY.get(),
                ModEntities.CANARY.get(),
                ModEntities.CARDINAL.get(),
                ModEntities.FINCH.get(),
                ModEntities.ROBIN.get(),
                ModEntities.SPARROW.get(),
                ModEntities.BROWN_BEAR.get(),
                ModEntities.BLACK_BEAR.get(),
                ModEntities.DEER.get(),
                ModEntities.REINDEER.get(),
                ModEntities.WHITE_DEER.get(),
                ModEntities.CAPYBARA.get(),
                ModEntities.HEDGEHOG.get(),
                ModEntities.KIWI.get(),
                ModEntities.PENGUIN.get(),
                ModEntities.SEAL.get(),
                ModEntities.SEA_URCHIN.get(),
                ModEntities.JELLYFISH.get(),
                ModEntities.JELLYFISH_2.get(),
                ModEntities.JELLYFISH_3.get(),
                ModEntities.SEAHORSE_1.get(),
                ModEntities.SHRIMP_1.get(),
                ModEntities.STARFISH_1.get(),
                ModEntities.DRAGONFLY.get(),
                ModEntities.DUMBO_OCTOPUS.get(),
                ModEntities.FERRET.get(),
                ModEntities.JUMPING_SPIDER.get(),
                ModEntities.KOI_FISH.get(),
                ModEntities.OTTER.get(),
                ModEntities.RED_PANDA.get(),
                ModEntities.SEA_BUNNY.get(),
                ModEntities.FIREFLY.get(),
                ModEntities.SMALL_FIREFLY.get()
        );
    }
}