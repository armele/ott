package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
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

    public ModItemTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, // FIXED TYPE HERE
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, blockTags, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // --- 1. DEFINE TAG KEYS ---
        TagKey<Item> ottConcreteKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "concrete"));
        TagKey<Item> ottConcretePowderKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "concrete_powder"));
        TagKey<Item> ottWoolKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "wool"));
        TagKey<Item> ottStainedGlassKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "stained_glass"));
        TagKey<Item> ottTerracottaKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "terracotta"));

        TagKey<Item> cConcretesKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "concretes"));
        TagKey<Item> cConcretePowdersKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "concrete_powders"));
        TagKey<Item> cWoolKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "wool"));
        TagKey<Item> cTerracottaKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "terracotta"));
        TagKey<Item> cDyedKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyed"));
        TagKey<Item> cGlassKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "glass"));
        TagKey<Item> cGlassBlocksKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "glass_blocks"));
        TagKey<Item> cGlassBlocksCheapKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "glass_blocks_cheap"));
        TagKey<Item> cGlassBlocksColoredKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "glass_blocks/colored"));

        TagKey<Item> mcTier1Key = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecolonies", "tier1blocks"));
        TagKey<Item> mcTier2Key = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecolonies", "tier2blocks"));

        // NEW: Linking Concrete Powder to Structurize weak blocks
        TagKey<Item> structurizeWeakKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("structurize", "weak_solid_blocks"));

        // --- 2. COPY FROM BLOCKS ---
        // This copies the contents of your ott: block tags into these ott: item tags
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "concrete")), ottConcreteKey);
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "concrete_powder")), ottConcretePowderKey);
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "wool")), ottWoolKey);
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "stained_glass")), ottStainedGlassKey);
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "terracotta")), ottTerracottaKey);

        // --- 3. BUILD HIERARCHY ---
        this.tag(cConcretesKey).addTag(ottConcreteKey);
        this.tag(cConcretePowdersKey).addTag(ottConcretePowderKey);
        this.tag(cWoolKey).addTag(ottWoolKey);
        this.tag(cTerracottaKey).addTag(ottTerracottaKey);
        this.tag(cGlassKey).addTag(ottStainedGlassKey);
        this.tag(cGlassBlocksKey).addTag(ottStainedGlassKey);
        this.tag(cGlassBlocksCheapKey).addTag(ottStainedGlassKey);
        this.tag(cGlassBlocksColoredKey).addTag(ottStainedGlassKey);

        this.tag(cDyedKey)
                .addTag(ottConcreteKey)
                .addTag(ottConcretePowderKey)
                .addTag(ottWoolKey)
                .addTag(ottStainedGlassKey)
                .addTag(ottTerracottaKey);

        // MineColonies Hierarchy
        this.tag(mcTier1Key).addTag(ottWoolKey).addTag(ottTerracottaKey);
        this.tag(mcTier2Key).addTag(ottConcreteKey).addTag(ottConcretePowderKey).addTag(ottWoolKey).addTag(ottStainedGlassKey).addTag(ottTerracottaKey);

        this.tag(structurizeWeakKey).addTag(ottConcretePowderKey);

        // --- 4. VANILLA BACKPORTS ---
        this.tag(ModTags.Items.PALE_OAK_LOGS).add(
                ModBlocks.PALE_OAK_LOG.asItem(),
                ModBlocks.STRIPPED_PALE_OAK_LOG.asItem(),
                ModBlocks.PALE_OAK_WOOD.asItem(),
                ModBlocks.STRIPPED_PALE_OAK_WOOD.asItem()
        );
        this.tag(ItemTags.LOGS_THAT_BURN).addTag(ModTags.Items.PALE_OAK_LOGS);
        this.tag(ItemTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.asItem());

        // --- 5. WOOD SETS ---
        ModBlocks.WOOD_SETS.forEach((setName, set) -> {
            this.tag(ModTags.Items.woodSetLogs(setName)).add(set.log().asItem(), set.wood().asItem(), set.strippedLog().asItem(), set.strippedWood().asItem());
            this.tag(ItemTags.LOGS_THAT_BURN).addTag(ModTags.Items.woodSetLogs(setName));
            this.tag(ItemTags.PLANKS).add(set.planks().asItem());
            this.tag(ItemTags.WOODEN_SLABS).add(set.slab().asItem());
            this.tag(ItemTags.WOODEN_STAIRS).add(set.stairs().asItem());
            this.tag(ItemTags.WOODEN_FENCES).add(set.fence().asItem());
            this.tag(ItemTags.FENCE_GATES).add(set.fenceGate().asItem());
            this.tag(ItemTags.WOODEN_DOORS).add(set.door().asItem());
            this.tag(ItemTags.WOODEN_TRAPDOORS).add(set.trapdoor().asItem());
            this.tag(ItemTags.WOODEN_BUTTONS).add(set.button().asItem());
            this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(set.pressurePlate().asItem());
            this.tag(ItemTags.SIGNS).add(ModItems.WOOD_SET_SIGNS.get(setName).get());
            this.tag(ItemTags.HANGING_SIGNS).add(ModItems.WOOD_SET_HANGING_SIGNS.get(setName).get());
            this.tag(ItemTags.BOATS).add(ModItems.WOOD_SET_BOATS.get(setName).get());
            this.tag(ItemTags.CHEST_BOATS).add(ModItems.WOOD_SET_CHEST_BOATS.get(setName).get());
        });

        // --- 6. INDIVIDUALS ---
        this.tag(ItemTags.TRIM_MATERIALS).add(ModItems.RESIN_BRICK.get());
        this.tag(ItemTags.SIGNS).add(ModItems.PALE_OAK_SIGN.get());
        this.tag(ItemTags.HANGING_SIGNS).add(ModItems.PALE_OAK_HANGING_SIGN.get());
        this.tag(ItemTags.BOATS).add(ModItems.PALE_OAK_BOAT.get());
        this.tag(ItemTags.CHEST_BOATS).add(ModItems.PALE_OAK_CHEST_BOAT.get());
    }
}