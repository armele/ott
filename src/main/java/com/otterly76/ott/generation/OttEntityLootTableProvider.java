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
        this.add(ModEntities.DEER.get(), LootTable.lootTable());
        this.add(ModEntities.REINDEER.get(), LootTable.lootTable());
        this.add(ModEntities.WHITE_DEER.get(), LootTable.lootTable());

        this.add(ModEntities.CAPYBARA.get(), LootTable.lootTable());
        this.add(ModEntities.HEDGEHOG.get(), LootTable.lootTable());
        this.add(ModEntities.KIWI.get(), LootTable.lootTable());
        this.add(ModEntities.PENGUIN.get(), LootTable.lootTable());
        this.add(ModEntities.SEAL.get(), LootTable.lootTable());
        this.add(ModEntities.JELLYFISH.get(), createJellyfishDrops());
        this.add(ModEntities.JELLYFISH_2.get(), createJellyfishDrops());
        this.add(ModEntities.JELLYFISH_3.get(), createJellyfishDrops());
        this.add(ModEntities.SEA_URCHIN.get(), createSeaUrchinDrops());
        this.add(ModEntities.SHRIMP_1.get(), createMeatDrops(ModItems.RAW_SHRIMP.get()));
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

        this.add(ModEntities.BEAVER.get(), LootTable.lootTable());
        this.add(ModEntities.CHUPACABRA.get(), createDrops(Items.BONE, 1, 3));
        this.add(ModEntities.COUGAR.get(), LootTable.lootTable());
        this.add(ModEntities.COYOTE.get(), LootTable.lootTable());
        this.add(ModEntities.HOWLER.get(), createDrops(Items.ROTTEN_FLESH, 1, 3));
        this.add(ModEntities.MARMOT.get(), LootTable.lootTable());
        this.add(ModEntities.MOUSE.get(), LootTable.lootTable());
        this.add(ModEntities.PIT_VIPER.get(), createDrops(Items.BONE, 1, 1));
        this.add(ModEntities.RATTLESNAKE.get(), createDrops(Items.BONE, 1, 1));
        this.add(ModEntities.RINGTAIL.get(), LootTable.lootTable());
        this.add(ModEntities.SASQUATCH.get(), LootTable.lootTable());
        this.add(ModEntities.SKINWALKER.get(), createDrops(Items.ROTTEN_FLESH, 1, 3));
        this.add(ModEntities.SNAKE.get(), LootTable.lootTable());
        this.add(ModEntities.SQUONK.get(), createDrops(Items.GHAST_TEAR, 1, 2));
        this.add(ModEntities.TURKEY.get(), createTurkeyDrops());
        this.add(ModEntities.WECHUGE.get(), createDrops(Items.ROTTEN_FLESH, 1, 3));
        this.add(ModEntities.WENDIGO.get(), createDrops(Items.ROTTEN_FLESH, 2, 5));
        this.add(ModEntities.WOLVERINE.get(), LootTable.lootTable());

        this.add(ModEntities.CICHLID.get(), createMeatDrops(ModItems.RAW_CICHLID.get(), 1, 1));
        this.add(ModEntities.LEOPARD_CAT.get(), LootTable.lootTable());
        this.add(ModEntities.ECHIDNA.get(), LootTable.lootTable());
        this.add(ModEntities.GUITARFISH.get(), createMeatDrops(ModItems.RAW_GUITARFISH.get(), 1, 2));
        this.add(ModEntities.BONNETHEAD_SHARK.get(), createMeatDrops(ModItems.RAW_BONNETHEAD.get(), 1, 3));
        this.add(ModEntities.BURROWING_OWL.get(), createBirdDrops());
        this.add(ModEntities.BUSHDOG.get(), LootTable.lootTable());
        this.add(ModEntities.QUAIL.get(), LootTable.lootTable());
        this.add(ModEntities.CANDYCANE_SNAIL.get(), createMeatDrops(ModItems.RAW_SNAIL.get(), 1, 1));
        this.add(ModEntities.FIRE_SALAMANDER.get(), createDrops(Items.SLIME_BALL, 0, 1));
        this.add(ModEntities.RIVER_TURTLE.get(), createDrops(Items.TURTLE_SCUTE, 0, 1));
        this.add(ModEntities.GOBLIN_SHARK.get(), createMeatDrops(ModItems.RAW_GOBLIN_SHARK.get(), 1, 4));
        this.add(ModEntities.GUINEA_FOWL.get(), LootTable.lootTable());
        this.add(ModEntities.IMPALA.get(), LootTable.lootTable());
        this.add(ModEntities.MANTA_RAY.get(), createMeatDrops(ModItems.RAW_BONNETHEAD.get(), 2, 5));
        this.add(ModEntities.STORK.get(), LootTable.lootTable());
        this.add(ModEntities.MOLE.get(), createDrops(Items.DIRT, 0, 1));
        this.add(ModEntities.TREE_KANGAROO.get(), LootTable.lootTable());
        this.add(ModEntities.PALLAS_CAT.get(), LootTable.lootTable());
        this.add(ModEntities.PINK_LAND_IGUANA.get(), LootTable.lootTable());
        this.add(ModEntities.PSYCHO_JELLY.get(), createJellyfishDrops());
        this.add(ModEntities.SPOONBILL.get(), LootTable.lootTable());
        this.add(ModEntities.GIANT_SOFTSHELL_TURTLE.get(), createDrops(Items.TURTLE_SCUTE, 1, 2));

        this.add(ModEntities.BASS.get(), createFishDrops(ModItems.BASS.get()));
        this.add(ModEntities.CATFISH.get(), createFishDrops(ModItems.CATFISH.get()));
        this.add(ModEntities.WATER_BUFFALO.get(), createWaterBuffaloDrops());
        this.add(ModEntities.COCONUT_CRAB.get(), createCrabDrops());
        this.add(ModEntities.SAND_CRAB.get(), createCrabDrops());
        this.add(ModEntities.FIDDLER_CRAB.get(), createCrabDrops());
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
        return LootTable.lootTable();
    }

    private LootTable.Builder createJumpingSpiderDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SILK.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))));
    }

    private LootTable.Builder createKoiFishDrops() {
        return createFishDrops(ModItems.KOI_FISH.get());
    }

    private LootTable.Builder createFishDrops(net.minecraft.world.item.Item fish) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(fish)))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.BONE_MEAL))
                        .when(net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance(0.05F)));
    }

    private LootTable.Builder createWaterBuffaloDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.LEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F)))
                                .apply(net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 2.0F)))));
    }

    private LootTable.Builder createCrabDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.CRAB_CLAW.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))));
    }

    private LootTable.Builder createMeatDrops(net.minecraft.world.item.Item meat) {
        return createMeatDrops(meat, 1.0F, 3.0F);
    }

    private LootTable.Builder createMeatDrops(net.minecraft.world.item.Item meat, float min, float max) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(meat)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)))))));
    }

    private LootTable.Builder createDrops(net.minecraft.world.item.Item item, float min, float max) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))));
    }

    private LootTable.Builder createTurkeyDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))));
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
                        .add(LootItem.lootTableItem(net.minecraft.world.item.Items.RABBIT_HIDE)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))));
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
                ModEntities.SMALL_FIREFLY.get(),
                ModEntities.BEAVER.get(),
                ModEntities.CHUPACABRA.get(),
                ModEntities.COUGAR.get(),
                ModEntities.COYOTE.get(),
                ModEntities.HOWLER.get(),
                ModEntities.MARMOT.get(),
                ModEntities.MOUSE.get(),
                ModEntities.PIT_VIPER.get(),
                ModEntities.RATTLESNAKE.get(),
                ModEntities.RINGTAIL.get(),
                ModEntities.SASQUATCH.get(),
                ModEntities.SKINWALKER.get(),
                ModEntities.SNAKE.get(),
                ModEntities.SQUONK.get(),
                ModEntities.TURKEY.get(),
                ModEntities.WECHUGE.get(),
                ModEntities.WENDIGO.get(),
                ModEntities.WOLVERINE.get(),
                ModEntities.CICHLID.get(),
                ModEntities.LEOPARD_CAT.get(),
                ModEntities.ECHIDNA.get(),
                ModEntities.GUITARFISH.get(),
                ModEntities.BONNETHEAD_SHARK.get(),
                ModEntities.BURROWING_OWL.get(),
                ModEntities.BUSHDOG.get(),
                ModEntities.QUAIL.get(),
                ModEntities.CANDYCANE_SNAIL.get(),
                ModEntities.FIRE_SALAMANDER.get(),
                ModEntities.RIVER_TURTLE.get(),
                ModEntities.GOBLIN_SHARK.get(),
                ModEntities.GUINEA_FOWL.get(),
                ModEntities.IMPALA.get(),
                ModEntities.MANTA_RAY.get(),
                ModEntities.STORK.get(),
                ModEntities.MOLE.get(),
                ModEntities.TREE_KANGAROO.get(),
                ModEntities.PALLAS_CAT.get(),
                ModEntities.PINK_LAND_IGUANA.get(),
                ModEntities.PSYCHO_JELLY.get(),
                ModEntities.SPOONBILL.get(),
                ModEntities.GIANT_SOFTSHELL_TURTLE.get(),
                ModEntities.BASS.get(),
                ModEntities.CATFISH.get(),
                ModEntities.WATER_BUFFALO.get(),
                ModEntities.COCONUT_CRAB.get(),
                ModEntities.SAND_CRAB.get(),
                ModEntities.FIDDLER_CRAB.get()
        );
    }
}