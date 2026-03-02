package com.otterly76.ott.block.wood;

import com.google.common.collect.Maps;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class ModBlockFamilies {
    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();
    public static BlockFamily RESIN_BRICKS;
    public static BlockFamily PALE_OAK;

    public static BlockFamily.Builder familyBuilder(Block baseBlock) {
        return new BlockFamily.Builder(baseBlock);
    }

    private static BlockFamily registerFamily(BlockFamily family) {
        Block baseBlock = family.getBaseBlock();
        if (MAP.containsKey(baseBlock)) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(baseBlock));
        } else {
            MAP.put(baseBlock, family);
            return family;
        }
    }

    public static void createBlockFamilies() {
        RESIN_BRICKS = registerFamily(familyBuilder(ModBlocks.RESIN_BRICKS.get())
                .wall(ModBlocks.RESIN_BRICK_WALL.get())
                .stairs(ModBlocks.RESIN_BRICK_STAIRS.get())
                .slab(ModBlocks.RESIN_BRICK_SLAB.get())
                .chiseled(ModBlocks.CHISELED_RESIN_BRICKS.get())
                .getFamily());
        PALE_OAK = registerFamily(familyBuilder(ModBlocks.PALE_OAK_PLANKS.get())
                .button(ModBlocks.PALE_OAK_BUTTON.get())
                .fence(ModBlocks.PALE_OAK_FENCE.get())
                .fenceGate(ModBlocks.PALE_OAK_FENCE_GATE.get())
                .pressurePlate(ModBlocks.PALE_OAK_PRESSURE_PLATE.get())
                .sign(ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get())
                .slab(ModBlocks.PALE_OAK_SLAB.get())
                .stairs(ModBlocks.PALE_OAK_STAIRS.get())
                .door(ModBlocks.PALE_OAK_DOOR.get())
                .trapdoor(ModBlocks.PALE_OAK_TRAPDOOR.get())
                .recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily());
    }
}
