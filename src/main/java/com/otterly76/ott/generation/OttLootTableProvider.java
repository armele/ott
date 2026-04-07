package com.otterly76.ott.generation;

import com.otterly76.ott.block.BigLilyPadBlock;
import com.otterly76.ott.block.GradientStainedGlassBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CopperChestBlock;
import com.otterly76.ott.block.custom.CopperGolemStatueBlock;
import com.otterly76.ott.crop.ThornyHedgeSprouts;
import com.otterly76.ott.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
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
            } else if (block instanceof ThornyHedgeSprouts) {
                this.add(block, createCropDrops(
                        block,
                        ModItems.THORNY_HEDGE_SPROUTS.get(),
                        ModItems.THORNY_HEDGE.get().asItem(),
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ThornyHedgeSprouts.AGE, ThornyHedgeSprouts.MAX_AGE))
                ));
            } else if (block instanceof DoorBlock) {
                this.add(block, this::createDoorTable);
            } else if (block instanceof BigLilyPadBlock) {
                this.add(block, (b) -> LootTable.lootTable().withPool(this.applyExplosionCondition(b, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(b).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BigLilyPadBlock.POSITION, com.otterly76.ott.block.properties.QuadDirection.BOTTOM_LEFT)))))));
            } else if (block instanceof CopperChestBlock) {
                this.add(block, (b) -> LootTable.lootTable().withPool(this.applyExplosionCondition(b, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(b).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME))))));
            } else if (block instanceof CopperGolemStatueBlock) {
                this.add(block, (b) -> LootTable.lootTable().withPool(this.applyExplosionCondition(b, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(b).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME)).apply(CopyBlockState.copyState(b).copy(CopperGolemStatueBlock.POSE))))));
            } else if (block instanceof WallTorchBlock) {
                if (block == ModBlocks.COPPER_WALL_TORCH.get()) this.dropOther(block, ModBlocks.COPPER_TORCH.get());
                else this.dropSelf(block);
            } else if (block instanceof WallSkullBlock) {
                if (block == ModBlocks.DRAGON_WALL_SKULL.get()) this.dropOther(block, ModBlocks.DRAGON_SKULL.get());
                else this.dropSelf(block);
            } else if (block instanceof WallSignBlock) {
                this.dropOther(block, ModBlocks.PALE_OAK_SIGN.get());
            } else if (block instanceof WallHangingSignBlock) {
                this.dropOther(block, ModBlocks.PALE_OAK_HANGING_SIGN.get());
            } else if (block instanceof SlabBlock) {
                this.add(block, this::createSlabItemTable);
            } else if (block == ModBlocks.PALE_OAK_LEAVES.get()) {
                this.add(block, (b) -> this.createLeavesDrops(b, ModBlocks.PALE_OAK_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
            } else if (block == ModBlocks.PALE_HANGING_MOSS.get()) {
                this.add(block, this::createDoublePlantShearsDrop);
            } else if (block instanceof SaplingBlock || block instanceof FlowerBlock) {
                this.dropSelf(block);
            } else if (block instanceof FlowerPotBlock potted) {
                this.add(block, (b) -> this.createPotFlowerItemTable(potted.getPotted()));
            } else if (block == ModBlocks.PLAIN_LIMESTONE.get()) {
                this.add(block, createSingleItemTableWithSilkTouch(block, ModBlocks.COBBLED_LIMESTONE.get()));
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
        knownBlocks.addAll(ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).toList());
        knownBlocks.addAll(ModBlocks.MINECRAFT_BLOCKS.getEntries().stream()
                .map(Supplier::get).toList());
        return knownBlocks;
    }
}