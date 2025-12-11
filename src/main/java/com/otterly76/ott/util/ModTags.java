package com.otterly76.ott.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PALE_OAK_LOGS = createTag("pale_oak_logs");

        private static TagKey<Block> createTag(String name) {
            // Using "minecraft" namespace for backport parity
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", name));
        }
    }
}