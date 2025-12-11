package com.otterly76.ott.generation;

import com.otterly76.ott.block.GradientStainedGlassBlock;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class OttLootTableProvider extends BlockLootSubProvider {
    public OttLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // --- 1. EXISTING MOD LOGIC ---
        // Iterate over blocks to handle them specifically
        ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).forEach(block -> {
            // Example logic to handle different block types
            if (block instanceof GradientStainedGlassBlock) {
                this.add(block, this::createSilkTouchOnlyTable);
            } else if (block instanceof HedgeSprouts) {
                // Drop the sprout item always + Drop the Hedge block only if AGE is 3
                this.add(block, createCropDrops(
                        block, // The crop block itself
                        ModItems.HEDGE_SPROUTS.get(),  // The seed item (always drops)
                        ModItems.HEDGE.get().asItem(), // The produce item (drops when fully grown)
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HedgeSprouts.AGE, HedgeSprouts.MAX_AGE))
                ));
            } else {
                this.dropSelf(block);
            }
        });

        // --- 2. BACKPORTED PALE GARDEN LOGIC ---
        this.dropSelf(ModBlocks.RESIN_CLUMP.get());
        this.dropSelf(ModBlocks.RESIN_BLOCK.get());
        this.dropSelf(ModBlocks.RESIN_BRICKS.get());
        this.dropSelf(ModBlocks.RESIN_BRICK_STAIRS.get());
        this.dropSelf(ModBlocks.RESIN_BRICK_WALL.get());
        this.dropSelf(ModBlocks.CHISELED_RESIN_BRICKS.get());
        this.add(ModBlocks.RESIN_BRICK_SLAB.get(), (block) -> this.createSlabItemTable(ModBlocks.RESIN_BRICK_SLAB.get()));
        this.dropSelf(ModBlocks.PALE_OAK_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        this.dropSelf(ModBlocks.PALE_OAK_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_PALE_OAK_WOOD.get());
        this.dropSelf(ModBlocks.PALE_OAK_SAPLING.get());
        this.add(ModBlocks.PALE_OAK_LEAVES.get(), (block) -> this.createLeavesDrops(block, ModBlocks.PALE_OAK_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.PALE_OAK_PLANKS.get());
        this.add(ModBlocks.PALE_HANGING_MOSS.get(), (block) -> this.createDoublePlantShearsDrop(ModBlocks.PALE_HANGING_MOSS.get()));
        this.add(ModBlocks.OPEN_EYEBLOSSOM.get(), (block) -> this.createPotFlowerItemTable(ModBlocks.OPEN_EYEBLOSSOM.get()));
        this.add(ModBlocks.CLOSED_EYEBLOSSOM.get(), (block) -> this.createPotFlowerItemTable(ModBlocks.CLOSED_EYEBLOSSOM.get()));
        // Potted plants logic
        this.add(ModBlocks.POTTED_PALE_OAK_SAPLING.get(), (block) -> this.createPotFlowerItemTable(ModBlocks.PALE_OAK_SAPLING.get()));
        this.add(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(), (block) -> this.createPotFlowerItemTable(ModBlocks.CLOSED_EYEBLOSSOM.get()));
        this.add(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(), (block) -> this.createPotFlowerItemTable(ModBlocks.OPEN_EYEBLOSSOM.get()));

        this.dropSelf(ModBlocks.OPEN_EYEBLOSSOM.get());
        this.dropSelf(ModBlocks.CLOSED_EYEBLOSSOM.get());
        this.dropSelf(ModBlocks.PALE_MOSS_BLOCK.get());
        this.dropSelf(ModBlocks.PALE_MOSS_CARPET.get());
        this.dropSelf(ModBlocks.PALE_OAK_SLAB.get());
        this.dropSelf(ModBlocks.PALE_OAK_STAIRS.get());
        this.dropSelf(ModBlocks.PALE_OAK_FENCE.get());
        this.dropSelf(ModBlocks.PALE_OAK_FENCE_GATE.get());
        this.createDoorTable(ModBlocks.PALE_OAK_DOOR.get());
        this.dropSelf(ModBlocks.PALE_OAK_TRAPDOOR.get());
        this.dropSelf(ModBlocks.PALE_OAK_BUTTON.get());
        this.dropSelf(ModBlocks.PALE_OAK_PRESSURE_PLATE.get());
        this.dropOther(ModBlocks.PALE_OAK_SIGN.get(), ModItems.PALE_OAK_SIGN.get());
        this.dropOther(ModBlocks.PALE_OAK_WALL_SIGN.get(), ModItems.PALE_OAK_SIGN.get());
        this.dropOther(ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModItems.PALE_OAK_HANGING_SIGN.get());
        this.dropOther(ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get(), ModItems.PALE_OAK_HANGING_SIGN.get());
    }

    // Helper method to create a crop table that drops seeds + extra item when fully grown
    protected LootTable.Builder createCropDrops(Block cropBlock, net.minecraft.world.item.Item seedItem, net.minecraft.world.item.Item grownItem, LootItemBlockStatePropertyCondition.Builder condition) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(seedItem))) // Always drop the seed
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(grownItem) // Drop the produce
                                .when(condition)));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        List<Block> knownBlocks = new ArrayList<>();
        // Add existing blocks
        knownBlocks.addAll(ModBlocks.BLOCKS.getEntries().stream().map(block -> (Block) block.get()).toList());
        // Add new backported blocks
        knownBlocks.addAll(ModBlocks.MINECRAFT_BLOCKS.getEntries().stream().map(block -> (Block) block.get()).toList());
        return knownBlocks;
    }
}