package com.otterly76.ott.generation;

import com.otterly76.ott.block.GradientStainedGlassBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.crop.HedgeSprouts;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.block.custom.EyeblossomBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
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
import java.util.stream.Stream;

public class OttLootTableProvider extends BlockLootSubProvider {
    public OttLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        Stream.concat(
                ModBlocks.BLOCKS.getEntries().stream(),
                ModBlocks.MINECRAFT_BLOCKS.getEntries().stream()
        ).map(Supplier::get).forEach(block -> {
            if (block instanceof GradientStainedGlassBlock) {
                this.add(block, this::createSilkTouchOnlyTable);
            } else if (block instanceof HedgeSprouts) {
                this.add(block, createCropDrops(
                        block,
                        ModItems.HEDGE_SPROUTS.get(),
                        ModItems.HEDGE.get().asItem(),
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HedgeSprouts.AGE, HedgeSprouts.MAX_AGE))
                ));
            } else if (block instanceof DoorBlock) {
                this.add(block, this::createDoorTable);
            } else if (block instanceof SlabBlock) {
                this.add(block, this::createSlabItemTable);
            } else if (block == ModBlocks.PALE_OAK_LEAVES.get()) {
                this.add(block, (b) -> this.createLeavesDrops(b, ModBlocks.PALE_OAK_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
            } else if (block == ModBlocks.PALE_HANGING_MOSS.get()) {
                this.add(block, (b) -> this.createDoublePlantShearsDrop(b));
            } else if (block instanceof EyeblossomBlock || block instanceof SaplingBlock || block instanceof FlowerBlock) {
                this.dropSelf(block);
            } else if (block instanceof FlowerPotBlock potted) {
                this.add(block, (b) -> this.createPotFlowerItemTable(potted.getPotted()));
            } else {
                this.dropSelf(block);
            }
        });
    }

    protected LootTable.Builder createCropDrops(Block cropBlock, net.minecraft.world.item.Item seedItem, net.minecraft.world.item.Item grownItem, LootItemBlockStatePropertyCondition.Builder condition) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(seedItem)))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(grownItem)
                                .when(condition)));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        List<Block> knownBlocks = new ArrayList<>();
        knownBlocks.addAll(ModBlocks.BLOCKS.getEntries().stream().map(block -> (Block) block.get()).toList());
        knownBlocks.addAll(ModBlocks.MINECRAFT_BLOCKS.getEntries().stream()
                .map(block -> (Block) block.get()).toList());
        return knownBlocks;
    }
}