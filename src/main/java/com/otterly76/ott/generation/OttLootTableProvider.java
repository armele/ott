package com.otterly76.ott.generation;

import com.otterly76.ott.block.GradientStainedGlassBlock;
import com.otterly76.ott.block.HedgeBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.crop.HedgeSprouts;
import com.otterly76.ott.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.Supplier;

public class OttLootTableProvider extends BlockLootSubProvider {
    public OttLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // Iterate over blocks to handle them specifically
        ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).forEach(block -> {
            // Example logic to handle different block types
            switch (block) {
                case GradientStainedGlassBlock gradientStainedGlassBlock ->
                        this.add(block, this::createSilkTouchOnlyTable);
                case HedgeBlock hedgeBlock -> this.dropSelf(block);
                case HedgeSprouts hedgeSprouts ->
                    // Drop the sprout item always + Drop the Hedge block only if AGE is 3
                        this.add(block, createCropDrops(
                                ModBlocks.HEDGE_SPROUTS.get(), // The crop block itself
                                ModItems.HEDGE.get().asItem(), // The produce item (drops when fully grown)
                                ModItems.HEDGE_SPROUTS.get(),  // The seed item (always drops)
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HedgeSprouts.AGE, HedgeSprouts.MAX_AGE))
                        ));
                default -> this.dropSelf(block);
            }
        });
    }

    // Helper method to create a crop table that drops seeds + extra item when fully grown
    protected LootTable.Builder createCropDrops(Block cropBlock, net.minecraft.world.item.Item seedItem, net.minecraft.world.item.Item grownItem, net.minecraft.world.level.block.state.properties.IntegerProperty ageProperty) {
        int maxAge = java.util.Collections.max(ageProperty.getPossibleValues());

        // Condition: Is the crop fully grown?
        LootItemBlockStatePropertyCondition.Builder isFullyGrown = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ageProperty, maxAge));

        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(seedItem))) // Always drop the seed/sprout item
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(grownItem) // Drop the Hedge Block
                                .when(isFullyGrown))); // ONLY when fully grown
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(block -> (Block) block.get()).toList();
    }
}