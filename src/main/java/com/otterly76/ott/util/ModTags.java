package com.otterly76.ott.util;

import com.otterly76.ott.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PALE_OAK_LOGS = BlockTags.create(ResourceLocation.withDefaultNamespace("pale_oak_logs"));
        public static final TagKey<Block> STONE = BlockTags.create(ResourceLocation.withDefaultNamespace("stone"));
        public static final TagKey<Block> PATHS = createTag("paths");
        public static final TagKey<Block> HARVEST_BLACKLIST = createTag("harvest_blacklist");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> PALE_OAK_LOGS = createTag("pale_oak_logs");

        public static final TagKey<Item> DYEABLE_BANNERS = createTag("dyeable_banners");
        public static final TagKey<Item> DYEABLE_CANDLES = createTag("dyeable_candles");
        public static final TagKey<Item> DYEABLE_GLASS_BLOCKS = createTag("dyeable_glass_blocks");
        public static final TagKey<Item> DYEABLE_GLASS_PANES = createTag("dyeable_glass_panes");
        public static final TagKey<Item> DYEABLE_SHULKER_BOXES = createTag("dyeable_shulker_boxes");
        public static final TagKey<Item> DYEABLE_CONCRETE = createTag("dyeable_concrete");
        public static final TagKey<Item> DYEABLE_CONCRETE_POWDER = createTag("dyeable_concrete_powder");
        public static final TagKey<Item> DYEABLE_TERRACOTTA = createTag("dyeable_terracotta");
        public static final TagKey<Item> INVENTORY_OPENABLE = createTag("inventory_openable");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        public static TagKey<Item> woodSetLogs(String setName) {
            return createTag(setName + "_logs");
        }
    }
}