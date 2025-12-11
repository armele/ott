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
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public static final TagKey<Block> PALE_OAK_LOGS = createTag("pale_oak_logs");

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "ott", existingFileHelper);
    }

    private static TagKey<Block> createTag(String name) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", name));
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.addVanillaTags();
        this.tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add((Block)ModBlocks.RESIN_CLUMP.get());
        this.tag(BlockTags.WALLS).add((Block)ModBlocks.RESIN_BRICK_WALL.get());
        this.tag(BlockTags.SLABS).add((Block)ModBlocks.RESIN_BRICK_SLAB.get());
        this.tag(BlockTags.STAIRS).add((Block)ModBlocks.RESIN_BRICK_STAIRS.get());
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(PALE_OAK_LOGS);
        this.tag(BlockTags.LEAVES).add((Block)ModBlocks.PALE_OAK_LEAVES.get());
        this.tag(BlockTags.SAPLINGS).add((Block)ModBlocks.PALE_OAK_SAPLING.get());
        this.tag(BlockTags.PLANKS).add((Block)ModBlocks.PALE_OAK_PLANKS.get());
        this.tag(PALE_OAK_LOGS).add(new Block[]{(Block)ModBlocks.PALE_OAK_LOG.get(), (Block)ModBlocks.STRIPPED_PALE_OAK_LOG.get(), (Block)ModBlocks.PALE_OAK_WOOD.get(), (Block)ModBlocks.STRIPPED_PALE_OAK_WOOD.get()});
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(new Block[]{(Block)ModBlocks.RESIN_BRICKS.get(), (Block)ModBlocks.CHISELED_RESIN_BRICKS.get(), (Block)ModBlocks.RESIN_BRICK_SLAB.get(), (Block)ModBlocks.RESIN_BLOCK.get(), (Block)ModBlocks.RESIN_BRICK_STAIRS.get(), (Block)ModBlocks.RESIN_BRICK_WALL.get()});
        this.tag(BlockTags.MINEABLE_WITH_AXE).add((Block)ModBlocks.CREAKING_HEART.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(new Block[]{(Block)ModBlocks.PALE_MOSS_BLOCK.get(), (Block)ModBlocks.PALE_MOSS_CARPET.get()});
        this.tag(BlockTags.FLOWERS).add(new Block[]{(Block)ModBlocks.CLOSED_EYEBLOSSOM.get(), (Block)ModBlocks.OPEN_EYEBLOSSOM.get()});
        this.tag(BlockTags.SMALL_FLOWERS).add(new Block[]{(Block)ModBlocks.CLOSED_EYEBLOSSOM.get(), (Block)ModBlocks.OPEN_EYEBLOSSOM.get()});
        this.tag(BlockTags.WOODEN_BUTTONS).add((Block)ModBlocks.PALE_OAK_BUTTON.get());
        this.tag(BlockTags.WOODEN_DOORS).add((Block) ModBlocks.PALE_OAK_DOOR.get());
        this.tag(BlockTags.WOODEN_FENCES).add((Block)ModBlocks.PALE_OAK_FENCE.get());
        this.tag(BlockTags.WOODEN_SLABS).add((Block)ModBlocks.PALE_OAK_SLAB.get());
        this.tag(BlockTags.WOODEN_TRAPDOORS).add((Block)ModBlocks.PALE_OAK_TRAPDOOR.get());
        this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add((Block)ModBlocks.PALE_OAK_PRESSURE_PLATE.get());
        this.tag(BlockTags.WOODEN_STAIRS).add((Block)ModBlocks.PALE_OAK_STAIRS.get());
        this.tag(BlockTags.STANDING_SIGNS).add((Block)ModBlocks.PALE_OAK_SIGN.get());
        this.tag(BlockTags.WALL_SIGNS).add((Block)ModBlocks.PALE_OAK_WALL_SIGN.get());
        this.tag(BlockTags.CEILING_HANGING_SIGNS).add((Block)ModBlocks.PALE_OAK_HANGING_SIGN.get());
        this.tag(BlockTags.WALL_HANGING_SIGNS).add((Block)ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get());
        this.tag(BlockTags.FENCE_GATES).add((Block)ModBlocks.PALE_OAK_FENCE_GATE.get());
    }

    protected void addVanillaTags() {
    }
}