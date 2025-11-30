package com.otterly76.ott.generation;

import com.otterly76.ott.block.GradientStainedGlassBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.function.Supplier;

import com.otterly76.ott.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class OttLootTableProvider extends BlockLootSubProvider {
    public OttLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // Iterate over blocks to handle them specifically
        ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).map(block -> (Block) block).forEach(block -> {
            // Example logic to handle different block types
            if (block instanceof GradientStainedGlassBlock) {
                this.add(block, this::createSilkTouchOnlyTable);
            } else {
                this.dropSelf(block);
            }
        });
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(block -> (Block) block.get()).toList();
    }
}