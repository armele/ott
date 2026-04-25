package com.otterly76.ott.engraving;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngravingPalette {

    private static final Map<Item, List<ItemStack>> PALETTES = new HashMap<>();

    static {
        register(Blocks.STONE.asItem(),
                ModBlocks.ANGRY_STONE,
                ModBlocks.BLANK_STONE_CARVING,
                ModBlocks.BORDERED_STONE,
                ModBlocks.BRICK_BORDERED_STONE,
                ModBlocks.CARVED_STONE,
                ModBlocks.CHECKERED_STONE_TILES,
                ModBlocks.COBBLED_STONE,
                ModBlocks.CRACKED_DISORDERED_STONE_BRICKS,
                ModBlocks.CRACKED_FLAT_STONE_TILES,
                ModBlocks.CREEPER_STONE_CARVING,
                ModBlocks.CRYING_STONE,
                ModBlocks.CURLY_STONE_PILLAR,
                ModBlocks.CUT_BLANK_STONE,
                ModBlocks.CUT_STONE_COLUMN,
                ModBlocks.DUH_STONE,
                ModBlocks.EDGED_STONE_BRICKS,
                ModBlocks.ENGRAVED_STONE,
                ModBlocks.ETCHED_STONE_BRICKS,
                ModBlocks.FINE_STONE_PILLAR,
                ModBlocks.FLAT_STONE_TILES,
                ModBlocks.GLAD_STONE,
                ModBlocks.INLAYED_STONE,
                ModBlocks.INSCRIBED_STONE,
                ModBlocks.LAYED_STONE_BRICKS,
                ModBlocks.LODED_STONE,
                ModBlocks.MASSIVE_STONE_BRICKS,
                ModBlocks.OFFSET_STONE_BRICKS,
                ModBlocks.ORNATE_STONE_PILLAR,
                ModBlocks.OVERLAPPING_STONE_TILES,
                ModBlocks.PILLAR_STONE_BRICKS,
                ModBlocks.POLISHED_STONE,
                ModBlocks.PRISMAL_STONE_REMNANTS,
                ModBlocks.ROUGH_STONE,
                ModBlocks.ROUNDED_STONE_BRICKS,
                ModBlocks.RUNIC_CARVED_STONE,
                ModBlocks.SAD_STONE,
                ModBlocks.SANDED_STONE,
                ModBlocks.SIMPLE_STONE_PILLAR,
                ModBlocks.SMALL_STONE_BRICKS,
                ModBlocks.SMOOTH_INLAYED_STONE,
                ModBlocks.SMOOTH_STONE_COLUMN,
                ModBlocks.SMOOTHED_DOUBLE_INLAYED_STONE,
                ModBlocks.SPIDER_STONE_CARVING,
                ModBlocks.SPIRALED_STONE,
                ModBlocks.STACKED_STONE_BRICKS,
                ModBlocks.STONE_MINI_TILES,
                ModBlocks.STONE_PILLAR,
                ModBlocks.STONE_SCALES,
                ModBlocks.THICK_INLAYED_STONE,
                ModBlocks.TILED_BORDERED_STONE,
                ModBlocks.TILED_STONE,
                ModBlocks.TILED_STONE_COLUMN,
                ModBlocks.TINY_BRICK_BORDERED_STONE,
                ModBlocks.TINY_LAYERED_STONE_BRICKS,
                ModBlocks.TINY_LAYERED_STONE_SLABS,
                ModBlocks.TINY_STONE_BRICKS,
                ModBlocks.TRODDEN_STONE,
                ModBlocks.UNAMUSED_STONE,
                ModBlocks.VERTICAL_CUT_STONE,
                ModBlocks.VERTICAL_DISORDERED_STONE_BRICKS,
                ModBlocks.WEATHERED_STONE,
                ModBlocks.SMOOTH_STONE_CTM
        );
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(Item input, net.neoforged.neoforge.registries.DeferredBlock<?>... outputs) {
        List<ItemStack> stacks = new ArrayList<>();
        for (var output : outputs) {
            stacks.add(new ItemStack(output.get().asItem()));
        }
        PALETTES.put(input, stacks);
    }

    public static List<ItemStack> getResults(Item input) {
        return PALETTES.getOrDefault(input, List.of());
    }
}
