package com.otterly76.ott.client.registries;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.client.gui.BundledTabs;
import com.otterly76.ott.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class ModBundledTabs {
    private static final List<BundledTabs> TABS = new ArrayList<>();

    public static List<BundledTabs> getTabs() {
        return TABS;
    }

    public static void bootstrap() {
        TABS.add(BundledTabs.builder()
                .title(Component.translatable("bundled_tab.ott.pale_oak"))
                .icon(new ItemStack(ModBlocks.PALE_OAK_LOG.get()))
                .displayItems((provider, output) -> {
                    output.accept(ModBlocks.PALE_OAK_LOG);
                    output.accept(ModBlocks.PALE_OAK_WOOD);
                    output.accept(ModBlocks.STRIPPED_PALE_OAK_LOG);
                    output.accept(ModBlocks.STRIPPED_PALE_OAK_WOOD);
                    output.accept(ModBlocks.PALE_OAK_PLANKS);
                    output.accept(ModBlocks.PALE_OAK_STAIRS);
                    output.accept(ModBlocks.PALE_OAK_SLAB);
                    output.accept(ModBlocks.PALE_OAK_FENCE);
                    output.accept(ModBlocks.PALE_OAK_FENCE_GATE);
                    output.accept(ModBlocks.PALE_OAK_DOOR);
                    output.accept(ModBlocks.PALE_OAK_TRAPDOOR);
                    output.accept(ModBlocks.PALE_OAK_PRESSURE_PLATE);
                    output.accept(ModBlocks.PALE_OAK_BUTTON);
                    output.accept(ModItems.PALE_OAK_SIGN);
                    output.accept(ModItems.PALE_OAK_HANGING_SIGN);
                    output.accept(ModBlocks.PALE_OAK_SAPLING);
                    output.accept(ModBlocks.PALE_OAK_LEAVES);
                    output.accept(ModItems.PALE_OAK_BOAT);
                    output.accept(ModItems.PALE_OAK_CHEST_BOAT);
                }).build());

        TABS.add(BundledTabs.builder()
                .title(Component.translatable("bundled_tab.ott.pale_garden"))
                .icon(new ItemStack(ModBlocks.PALE_MOSS_BLOCK.get()))
                .displayItems((provider, output) -> {
                    output.accept(ModBlocks.PALE_MOSS_BLOCK);
                    output.accept(ModBlocks.PALE_MOSS_CARPET);
                    output.accept(ModBlocks.PALE_HANGING_MOSS);
                    output.accept(ModBlocks.OPEN_EYEBLOSSOM);
                    output.accept(ModBlocks.CLOSED_EYEBLOSSOM);
                    output.accept(ModBlocks.CREAKING_HEART);
                    output.accept(ModItems.CREAKING_SPAWN_EGG);
                }).build());

        TABS.add(BundledTabs.builder()
                .title(Component.translatable("bundled_tab.ott.resin"))
                .icon(new ItemStack(ModBlocks.RESIN_BLOCK.get()))
                .displayItems((provider, output) -> {
                    output.accept(ModBlocks.RESIN_BLOCK);
                    output.accept(ModBlocks.RESIN_BRICKS);
                    output.accept(ModBlocks.RESIN_BRICK_STAIRS);
                    output.accept(ModBlocks.RESIN_BRICK_SLAB);
                    output.accept(ModBlocks.RESIN_BRICK_WALL);
                    output.accept(ModBlocks.CHISELED_RESIN_BRICKS);
                    output.accept(ModItems.RESIN_BRICK);
                    output.accept(ModBlocks.RESIN_CLUMP);
                }).build());

        TABS.add(BundledTabs.builder()
                .title(Component.translatable("bundled_tab.ott.spring_to_life"))
                .icon(new ItemStack(ModBlocks.FIREFLY_BUSH.get()))
                .displayItems((provider, output) -> {
                    output.accept(ModBlocks.BUSH);
                    output.accept(ModBlocks.FIREFLY_BUSH);
                    output.accept(ModBlocks.WILDFLOWERS);
                    output.accept(ModBlocks.LEAF_LITTER);
                    output.accept(ModBlocks.CACTUS_FLOWER);
                    output.accept(ModBlocks.SHORT_DRY_GRASS);
                    output.accept(ModBlocks.TALL_DRY_GRASS);
                }).build());

        TABS.add(BundledTabs.builder()
                .title(Component.translatable("bundled_tab.ott.chase_the_skies"))
                .icon(new ItemStack(ModItems.HAPPY_GHAST_SPAWN_EGG.get()))
                .displayItems((provider, output) -> {
                    output.accept(ModItems.HAPPY_GHAST_SPAWN_EGG);
                    output.accept(ModItems.MUSIC_DISC_TEARS);
                    output.accept(ModItems.MUSIC_DISC_LAVA_CHICKEN);
                    ModItems.HARNESSES.values().forEach(output::accept);
                }).build());

        TABS.add(BundledTabs.builder()
                .title(Component.translatable("bundled_tab.ott.bundles_of_bravery"))
                .icon(new ItemStack(Items.BUNDLE))
                .displayItems((provider, output) -> {
                    output.accept(Items.BUNDLE);
                    ModItems.BUNDLES.values().forEach(output::accept);
                }).build());
    }
}