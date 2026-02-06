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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
        this.tag(ItemTags.LOGS).addTag(ModTags.Items.PALE_OAK_LOGS);
        this.tag(ItemTags.LOGS_THAT_BURN).addTag(ModTags.Items.PALE_OAK_LOGS);
        this.tag(ItemTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.asItem());
        this.tag(ItemTags.LEAVES).add(ModBlocks.PALE_OAK_LEAVES.asItem());

        this.tag(ItemTags.FENCE_GATES).add(ModBlocks.PALE_OAK_FENCE_GATE.asItem());
        this.tag(ItemTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.asItem());
        this.tag(ItemTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.asItem());
        this.tag(ItemTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.asItem());

        this.tag(ItemTags.WOODEN_BUTTONS).add(ModBlocks.PALE_OAK_BUTTON.asItem());
        this.tag(ItemTags.WOODEN_DOORS).add(ModBlocks.PALE_OAK_DOOR.asItem());
        this.tag(ItemTags.WOODEN_FENCES).add(ModBlocks.PALE_OAK_FENCE.asItem());
        this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.PALE_OAK_PRESSURE_PLATE.asItem());
        this.tag(ItemTags.WOODEN_SLABS).add(ModBlocks.PALE_OAK_SLAB.asItem());
        this.tag(ItemTags.WOODEN_STAIRS).add(ModBlocks.PALE_OAK_STAIRS.asItem());
        this.tag(ItemTags.WOODEN_TRAPDOORS).add(ModBlocks.PALE_OAK_TRAPDOOR.asItem());

        // --- 5. WOOD SETS ---
        ModBlocks.WOOD_SETS.forEach((setName, set) -> {
            this.tag(ModTags.Items.woodSetLogs(setName)).add(set.log().asItem(), set.wood().asItem(), set.strippedLog().asItem(), set.strippedWood().asItem());
            this.tag(ItemTags.LOGS).addTag(ModTags.Items.woodSetLogs(setName));
            this.tag(ItemTags.LOGS_THAT_BURN).addTag(ModTags.Items.woodSetLogs(setName));
            this.tag(ItemTags.PLANKS).add(set.planks().asItem());
            this.tag(ItemTags.LEAVES).add(set.leaves().asItem());
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

        this.tag(ItemTags.COALS).add(ModItems.TINY_COAL.get(), ModItems.TINY_CHARCOAL.get());

        // --- 7. DYEABLE ITEMS ---
        var dyeableBanners = this.tag(ModTags.Items.DYEABLE_BANNERS);
        var dyeableCandles = this.tag(ModTags.Items.DYEABLE_CANDLES);
        var dyeableGlassBlocks = this.tag(ModTags.Items.DYEABLE_GLASS_BLOCKS);
        var dyeableGlassPanes = this.tag(ModTags.Items.DYEABLE_GLASS_PANES);
        var dyeableShulkerBoxes = this.tag(ModTags.Items.DYEABLE_SHULKER_BOXES);
        var dyeableConcrete = this.tag(ModTags.Items.DYEABLE_CONCRETE);
        var dyeableConcretePowder = this.tag(ModTags.Items.DYEABLE_CONCRETE_POWDER);
        var dyeableTerracotta = this.tag(ModTags.Items.DYEABLE_TERRACOTTA);

        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            dyeableBanners.addOptional(ResourceLocation.withDefaultNamespace(name + "_banner"));
            dyeableCandles.addOptional(ResourceLocation.withDefaultNamespace(name + "_candle"));
            dyeableGlassBlocks.addOptional(ResourceLocation.withDefaultNamespace(name + "_stained_glass"));
            dyeableGlassPanes.addOptional(ResourceLocation.withDefaultNamespace(name + "_stained_glass_pane"));
            dyeableShulkerBoxes.addOptional(ResourceLocation.withDefaultNamespace(name + "_shulker_box"));
            dyeableConcrete.addOptional(ResourceLocation.withDefaultNamespace(name + "_concrete"));
            dyeableConcretePowder.addOptional(ResourceLocation.withDefaultNamespace(name + "_concrete_powder"));
            dyeableTerracotta.addOptional(ResourceLocation.withDefaultNamespace(name + "_terracotta"));
        }
        dyeableCandles.addOptional(ResourceLocation.withDefaultNamespace("candle"));
        dyeableGlassBlocks.addOptional(ResourceLocation.withDefaultNamespace("glass"));
        dyeableGlassPanes.addOptional(ResourceLocation.withDefaultNamespace("glass_pane"));
        dyeableShulkerBoxes.addOptional(ResourceLocation.withDefaultNamespace("shulker_box"));
        dyeableTerracotta.addOptional(ResourceLocation.withDefaultNamespace("terracotta"));

        this.tag(ModTags.Items.INVENTORY_OPENABLE)
                .addTag(ModTags.Items.DYEABLE_SHULKER_BOXES)
                .add(Items.BARREL)
                .add(Items.CRAFTING_TABLE)
                .add(Items.LOOM)
                .add(Items.CARTOGRAPHY_TABLE)
                .add(Items.GRINDSTONE)
                .add(Items.STONECUTTER)
                .add(Items.SMITHING_TABLE)
                .add(Items.ANVIL)
                .add(Items.CHIPPED_ANVIL)
                .add(Items.DAMAGED_ANVIL)
                .add(Items.ENDER_CHEST);

        this.tag(ItemTags.ARROWS).add(Items.ARROW, Items.TIPPED_ARROW, Items.SPECTRAL_ARROW, ModItems.TORCH_ARROW.get());
    }
}