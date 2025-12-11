package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public static final TagKey<Item> PALE_OAK_LOGS = createTag("pale_oak_logs");

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, "ott", existingFileHelper);
    }

    private static TagKey<Item> createTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }

    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.addVanillaTags();
        this.tag(PALE_OAK_LOGS).add(ModBlocks.PALE_OAK_LOG.asItem(), ModBlocks.STRIPPED_PALE_OAK_LOG.asItem(), ModBlocks.PALE_OAK_WOOD.asItem(), ModBlocks.STRIPPED_PALE_OAK_WOOD.asItem());
        this.tag(ItemTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.asItem());
        this.tag(ItemTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.asItem());
        this.tag(ItemTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.asItem());
        this.tag(ItemTags.TRIM_MATERIALS).add(ModItems.RESIN_BRICK.get());
        this.tag(ItemTags.LOGS_THAT_BURN).addTag(PALE_OAK_LOGS);
        this.tag(ItemTags.WOODEN_BUTTONS).add(ModBlocks.PALE_OAK_BUTTON.asItem());
        this.tag(ItemTags.WOODEN_DOORS).add(ModBlocks.PALE_OAK_DOOR.asItem());
        this.tag(ItemTags.WOODEN_FENCES).add(ModBlocks.PALE_OAK_FENCE.asItem());
        this.tag(ItemTags.WOODEN_SLABS).add(ModBlocks.PALE_OAK_SLAB.asItem());
        this.tag(ItemTags.WOODEN_STAIRS).add(ModBlocks.PALE_OAK_STAIRS.asItem());
        this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.PALE_OAK_PRESSURE_PLATE.asItem());
        this.tag(ItemTags.FENCE_GATES).add(ModBlocks.PALE_OAK_FENCE_GATE.asItem());
        this.tag(ItemTags.SIGNS).add(ModItems.PALE_OAK_SIGN.get());
        this.tag(ItemTags.HANGING_SIGNS).add(ModItems.PALE_OAK_HANGING_SIGN.get());
        this.tag(ItemTags.BOATS).add(ModItems.PALE_OAK_BOAT.get());
        this.tag(ItemTags.CHEST_BOATS).add(ModItems.PALE_OAK_CHEST_BOAT.get());
        this.tag(ItemTags.LEAVES).add(ModBlocks.PALE_OAK_LEAVES.asItem());
        this.tag(ItemTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.asItem());
    }

    protected void addVanillaTags() {
    }
}