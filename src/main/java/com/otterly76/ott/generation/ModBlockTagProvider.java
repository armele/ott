package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public static final TagKey<Block> PALE_OAK_LOGS = createTag();

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "ott", existingFileHelper);
    }

    private static TagKey<Block> createTag() {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "pale_oak_logs"));
    }

    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.addVanillaTags();
        this.tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(ModBlocks.RESIN_CLUMP.get());
        this.tag(BlockTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.get());
        this.tag(BlockTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.get());
        this.tag(BlockTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.get());
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(PALE_OAK_LOGS);

        this.tag(BlockTags.LEAVES).add(ModBlocks.PALE_OAK_LEAVES.get());
        this.tag(BlockTags.SAPLINGS).add(ModBlocks.PALE_OAK_SAPLING.get());
        this.tag(BlockTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.get());

        this.tag(PALE_OAK_LOGS).add(ModBlocks.PALE_OAK_LOG.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get(), ModBlocks.PALE_OAK_WOOD.get(), ModBlocks.STRIPPED_PALE_OAK_WOOD.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.RESIN_BRICKS.get(), ModBlocks.CHISELED_RESIN_BRICKS.get(), ModBlocks.RESIN_BRICK_SLAB.get(), ModBlocks.RESIN_BLOCK.get(), ModBlocks.RESIN_BRICK_STAIRS.get(), ModBlocks.RESIN_BRICK_WALL.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.CREAKING_HEART.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.PALE_MOSS_BLOCK.get(), ModBlocks.PALE_MOSS_CARPET.get());

        this.tag(BlockTags.DIRT).add(ModBlocks.PALE_MOSS_BLOCK.get());
        this.tag(BlockTags.REPLACEABLE_BY_TREES).add(ModBlocks.PALE_MOSS_BLOCK.get());

        this.tag(BlockTags.FLOWERS).add(ModBlocks.CLOSED_EYEBLOSSOM.get(), ModBlocks.OPEN_EYEBLOSSOM.get());
        this.tag(BlockTags.SMALL_FLOWERS).add(ModBlocks.CLOSED_EYEBLOSSOM.get(), ModBlocks.OPEN_EYEBLOSSOM.get());

        this.tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.PALE_OAK_BUTTON.get());
        this.tag(BlockTags.WOODEN_DOORS).add(ModBlocks.PALE_OAK_DOOR.get());
        this.tag(BlockTags.WOODEN_FENCES).add(ModBlocks.PALE_OAK_FENCE.get());
        this.tag(BlockTags.WOODEN_SLABS).add(ModBlocks.PALE_OAK_SLAB.get());
        this.tag(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.PALE_OAK_TRAPDOOR.get());
        this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.PALE_OAK_PRESSURE_PLATE.get());
        this.tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.PALE_OAK_STAIRS.get());
        this.tag(BlockTags.STANDING_SIGNS).add(ModBlocks.PALE_OAK_SIGN.get());
        this.tag(BlockTags.WALL_SIGNS).add(ModBlocks.PALE_OAK_WALL_SIGN.get());
        this.tag(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.PALE_OAK_HANGING_SIGN.get());
        this.tag(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get());
        this.tag(BlockTags.FENCE_GATES).add(ModBlocks.PALE_OAK_FENCE_GATE.get());
    }

    protected void addVanillaTags() {
    }
}