package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, blockTags, "ott", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.addVanillaTags();

        // --- backported pale oak grouping tag (ott namespace) ---
        this.tag(ModTags.Items.PALE_OAK_LOGS).add(
                ModBlocks.PALE_OAK_LOG.asItem(),
                ModBlocks.STRIPPED_PALE_OAK_LOG.asItem(),
                ModBlocks.PALE_OAK_WOOD.asItem(),
                ModBlocks.STRIPPED_PALE_OAK_WOOD.asItem()
        );

        // Vanilla tag hookups
        this.tag(ItemTags.LOGS_THAT_BURN).addTag(ModTags.Items.PALE_OAK_LOGS);

        // --- ott wood sets (e.g. starlight): per-set tag + vanilla hookups ---
        ModBlocks.WOOD_SETS.forEach((setName, set) -> {
            // per-set logs tag (ott:<set>_logs): "any log variant"
            this.tag(ModTags.Items.woodSetLogs(setName)).add(
                    set.log().asItem(),
                    set.wood().asItem(),
                    set.strippedLog().asItem(),
                    set.strippedWood().asItem()
            );

            // Make them behave like normal logs
            this.tag(ItemTags.LOGS_THAT_BURN).addTag(ModTags.Items.woodSetLogs(setName));

            // Common categories
            this.tag(ItemTags.PLANKS).add(set.planks().asItem());

            this.tag(ItemTags.WOODEN_SLABS).add(set.slab().asItem());
            this.tag(ItemTags.WOODEN_STAIRS).add(set.stairs().asItem());
            this.tag(ItemTags.WOODEN_FENCES).add(set.fence().asItem());
            this.tag(ItemTags.FENCE_GATES).add(set.fenceGate().asItem());
            this.tag(ItemTags.WOODEN_DOORS).add(set.door().asItem());
            this.tag(ItemTags.WOODEN_BUTTONS).add(set.button().asItem());
            this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(set.pressurePlate().asItem());

            // Sign item tags
            this.tag(ItemTags.SIGNS).add(ModItems.WOOD_SET_SIGNS.get(setName).get());
            this.tag(ItemTags.HANGING_SIGNS).add(ModItems.WOOD_SET_HANGING_SIGNS.get(setName).get());
            this.tag(ItemTags.BOATS).add(ModItems.WOOD_SET_BOATS.get(setName).get());
            this.tag(ItemTags.CHEST_BOATS).add(ModItems.WOOD_SET_CHEST_BOATS.get(setName).get());
        });

        // --- non-wood-set tags you already had ---
        this.tag(ItemTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.asItem());
        this.tag(ItemTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.asItem());
        this.tag(ItemTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.asItem());
        this.tag(ItemTags.TRIM_MATERIALS).add(ModItems.RESIN_BRICK.get());

        this.tag(ItemTags.SIGNS).add(ModItems.PALE_OAK_SIGN.get());
        this.tag(ItemTags.HANGING_SIGNS).add(ModItems.PALE_OAK_HANGING_SIGN.get());

        this.tag(ItemTags.BOATS).add(ModItems.PALE_OAK_BOAT.get());
        this.tag(ItemTags.CHEST_BOATS).add(ModItems.PALE_OAK_CHEST_BOAT.get());

        this.tag(ItemTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.asItem());
    }

    protected void addVanillaTags() {
    }
}