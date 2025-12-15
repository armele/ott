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
        public static final TagKey<Block> PALE_OAK_LOGS = createTag("pale_oak_logs");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> PALE_OAK_LOGS = createTag("pale_oak_logs");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        public static TagKey<Item> woodSetLogs(String setName) {
            return createTag(setName + "_logs");
        }
    }
}
// TODO go through and replace all instances of ott with MOD_ID