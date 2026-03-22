package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.color.ModColorSets;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
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

        TagKey<Item> doConcreteKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("domum_ornamentum", "concrete"));

        // NEW: Linking Concrete Powder to Structurize weak blocks
        TagKey<Item> structurizeWeakKey = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("structurize", "weak_solid_blocks"));

        // --- 2. COPY FROM BLOCKS ---
        // This copies the contents of your ott: block tags into these ott: item tags
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "concrete")), ottConcreteKey);
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "concrete_powder")), ottConcretePowderKey);
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "wool")), ottWoolKey);
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "stained_glass")), ottStainedGlassKey);
        this.copy(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "terracotta")), ottTerracottaKey);

        // --- 3. BUILD HIERARCHY (Inherited from blocks) ---
        copyCommonTag(cConcretesKey);
        copyCommonTag(cConcretePowdersKey);
        copyCommonTag(cWoolKey);
        copyCommonTag(cTerracottaKey);
        copyCommonTag(cGlassKey);
        copyCommonTag(cGlassBlocksKey);
        copyCommonTag(cGlassBlocksCheapKey);
        copyCommonTag(cGlassBlocksColoredKey);
        copyCommonTag(cDyedKey);

        // Vanilla Copies
        copyCommonTag(ItemTags.CANDLES);
        copyCommonTag(ItemTags.BEDS);
        copyCommonTag(ItemTags.WOOL_CARPETS);
        copyCommonTag(ItemTags.BANNERS);
        copyCommonTag(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("shulker_boxes")));
        copyCommonTag(ItemTags.WOOL);
        copyCommonTag(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("stained_glass")));
        copyCommonTag(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("stained_glass_panes")));
        copyCommonTag(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("concrete")));
        copyCommonTag(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("concrete_powder")));

        // MineColonies Hierarchy
        copyCommonTag(mcTier1Key);
        copyCommonTag(mcTier2Key);

        copyCommonTag(structurizeWeakKey);

        // Domum Ornamentum
        copyCommonTag(doConcreteKey);

        // --- 4. VANILLA BACKPORTS ---
        addWoodSetTags(
                ModTags.ItemTags.PALE_OAK_LOGS,
                ModBlocks.PALE_OAK_LOG,
                ModBlocks.PALE_OAK_WOOD,
                ModBlocks.STRIPPED_PALE_OAK_LOG,
                ModBlocks.STRIPPED_PALE_OAK_WOOD,
                ModBlocks.PALE_OAK_PLANKS,
                ModBlocks.PALE_OAK_LEAVES,
                ModBlocks.PALE_OAK_SLAB,
                ModBlocks.PALE_OAK_STAIRS,
                ModBlocks.PALE_OAK_FENCE,
                ModBlocks.PALE_OAK_FENCE_GATE,
                ModBlocks.PALE_OAK_DOOR,
                ModBlocks.PALE_OAK_TRAPDOOR,
                ModBlocks.PALE_OAK_BUTTON,
                ModBlocks.PALE_OAK_PRESSURE_PLATE,
                ModItems.PALE_OAK_SIGN.get(),
                ModItems.PALE_OAK_HANGING_SIGN.get(),
                ModItems.PALE_OAK_BOAT.get(),
                ModItems.PALE_OAK_CHEST_BOAT.get()
        );

        this.tag(net.minecraft.tags.ItemTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.asItem());
        this.tag(net.minecraft.tags.ItemTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.asItem());
        this.tag(net.minecraft.tags.ItemTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.asItem());

        this.tag(net.minecraft.tags.ItemTags.FISHES).add(
                ModItems.RAW_ANGELFISH.get(),
                ModItems.RAW_BARRELEYE.get(),
                ModItems.RAW_FLOUNDER.get(),
                ModItems.RAW_SUNFISH_MEAT.get(),
                ModItems.COOKED_SUNFISH_MEAT.get(),
                ModItems.RAW_GOLDEN_SUNFISH_MEAT.get(),
                ModItems.COOKED_GOLDEN_SUNFISH_MEAT.get(),
                ModItems.RAW_KRILL.get(),
                ModItems.FRIED_KRILL.get(),
                ModItems.RAW_SHRIMP_1.get(),
                ModItems.COOKED_SHRIMP_1.get(),
                ModItems.KOI_FISH.get(),
                ModItems.CATFISH.get(),
                ModItems.COOKED_CATFISH.get(),
                ModItems.BASS.get(),
                ModItems.COOKED_BASS.get()
        );

        this.tag(ModTags.ItemTags.EGGS).add(
                Items.EGG,
                ModItems.BLUE_EGG.get(),
                ModItems.BROWN_EGG.get(),
                ModItems.DUCK_EGG.get(),
                ModItems.PHEASANT_EGG.get(),
                ModItems.ALLIGATOR_EGG.get(),
                ModItems.TORTOISE_EGG.get(),
                ModItems.SNAIL_EGG.get(),
                ModItems.KIWI_EGG.get(),
                ModItems.PENGUIN_EGG.get(),
                ModItems.EMU_EGG.get(),
                ModItems.HOOPOE_EGG.get(),
                ModItems.TOUCAN_EGG.get()
        );

        this.tag(ModTags.ItemTags.FERRET_FOOD).add(
                ModItems.RAW_CAPYBARA.get(),
                ModItems.RAW_KIWI.get(),
                ModItems.RAW_SEAL.get(),
                ModItems.RAW_SHRIMP_1.get()
        );

        this.tag(ModTags.ItemTags.FERRET_TEMPT_ITEMS).add(
                Items.CHICKEN,
                Items.RABBIT
        );

        this.tag(ModTags.ItemTags.DRAGONFLY_FOOD).add(
                Items.SPIDER_EYE
        );

        this.tag(ModTags.ItemTags.JUMPING_SPIDER_FOOD).add(
                ModItems.DRAGONFLY_WING.get()
        );

        this.tag(ModTags.ItemTags.OTTER_FOOD).add(
                ModItems.CLAM.get(),
                ModItems.RAW_SHRIMP_1.get(),
                ModItems.KOI_FISH.get(),
                ModItems.RAW_ANGELFISH.get(),
                ModItems.RAW_BARRELEYE.get(),
                ModItems.RAW_FLOUNDER.get(),
                ModItems.RAW_SUNFISH_MEAT.get(),
                ModItems.RAW_GOLDEN_SUNFISH_MEAT.get(),
                ModItems.CATFISH.get(),
                ModItems.BASS.get(),
                ModItems.RAW_KRILL.get(),
                Items.COD,
                Items.SALMON,
                Items.TROPICAL_FISH,
                ModItems.COOKED_SHRIMP_1.get(),
                ModItems.FRIED_KRILL.get(),
                ModItems.COOKED_SUNFISH_MEAT.get(),
                ModItems.COOKED_GOLDEN_SUNFISH_MEAT.get(),
                ModItems.COOKED_CATFISH.get(),
                ModItems.COOKED_BASS.get(),
                Items.COOKED_COD,
                Items.COOKED_SALMON
        );

        this.tag(ModTags.ItemTags.RED_PANDA_FOOD).add(
                Items.BAMBOO,
                Items.SWEET_BERRIES
        );

        this.tag(ModTags.ItemTags.RED_PANDA_TEMPT_ITEMS).add(
                Items.BAMBOO
        );

        this.tag(ModTags.ItemTags.BIRD_FOOD_ITEMS).add(
                Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS,
                ModItems.RAW_SHRIMP_1.get()
        );

        this.tag(ModTags.ItemTags.ALLIGATOR_FOOD_ITEMS).add(
                Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.RABBIT,
                ModItems.RAW_CAPYBARA.get(),
                ModItems.RAW_KIWI.get(),
                ModItems.RAW_SEAL.get(),
                ModItems.RAW_SHRIMP_1.get()
        );

        this.tag(ModTags.ItemTags.CATFISH_FOOD_ITEMS).add(
                Items.TROPICAL_FISH, Items.COD, Items.TADPOLE_BUCKET,
                ModItems.RAW_ANGELFISH.get(),
                ModItems.RAW_SHRIMP_1.get()
        );

        this.tag(ModTags.ItemTags.VULTURE_FOOD_ITEMS).add(
                Items.ROTTEN_FLESH,
                ModItems.RAW_CAPYBARA.get(),
                ModItems.RAW_KIWI.get(),
                ModItems.RAW_SEAL.get(),
                ModItems.RAW_SHRIMP_1.get()
        );

        this.tag(ModTags.ItemTags.BEAR_FOOD_ITEMS).add(
                Items.SALMON, Items.HONEY_BOTTLE, Items.SWEET_BERRIES, Items.BEEF, Items.PORKCHOP, Items.CHICKEN
        );

        this.tag(ModTags.ItemTags.BOAR_FOOD_ITEMS).add(
                Items.WHEAT, Items.CARROT, Items.POTATO, Items.BEETROOT
        );

        this.tag(ModTags.ItemTags.DEER_FOOD_ITEMS).add(
                Items.APPLE, Items.WHEAT, Items.CARROT
        );

        this.tag(ModTags.ItemTags.DUCK_FOOD_ITEMS).add(
                Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS
        );

        this.tag(ModTags.ItemTags.FIREFLY_TEMPT_ITEMS).add(
                Items.GLASS_BOTTLE
        );

        this.tag(ModTags.ItemTags.LIZARD_FOOD_ITEMS).add(
                Items.SPIDER_EYE, Items.MELON_SEEDS
        );

        this.tag(ModTags.ItemTags.MOOSE_FOOD_ITEMS).add(
                Items.APPLE, Items.WHEAT
        );

        this.tag(ModTags.ItemTags.RHINO_FOOD_ITEMS).add(
                Items.WHEAT, Items.MELON_SLICE
        );

        this.tag(ModTags.ItemTags.SNAIL_FOOD_ITEMS).add(
                Items.MUSHROOM_STEW, Items.BROWN_MUSHROOM, Items.RED_MUSHROOM
        );

        this.tag(ModTags.ItemTags.TORTOISE_FOOD_ITEMS).add(
                Items.CACTUS, Items.MELON_SLICE
        );

        this.tag(ModTags.ItemTags.ZEBRA_FOOD_ITEMS).add(
                Items.APPLE, Items.WHEAT
        );

        this.tag(ModTags.ItemTags.CAPYBARA_FOOD).add(
                Items.MELON_SLICE, Items.APPLE, Items.SUGAR_CANE
        );

        this.tag(ModTags.ItemTags.HEDGEHOG_FOOD).add(
                Items.SWEET_BERRIES, Items.APPLE
        );

        this.tag(ModTags.ItemTags.KIWI_FOOD).add(
                Items.SWEET_BERRIES, Items.MELON_SLICE
        );

        this.tag(ModTags.ItemTags.PENGUIN_FOOD).add(
                Items.SALMON, Items.COD, ModItems.RAW_KRILL.get()
        );

        this.tag(ModTags.ItemTags.SEAL_FOOD).add(
                Items.SALMON, Items.COD, ModItems.RAW_KRILL.get()
        );

        this.tag(ModTags.ItemTags.SEA_URCHIN_FOOD).add(
                Items.KELP, Items.SEAGRASS
        );

        // --- 5. WOOD SETS ---
        ModBlocks.WOOD_SETS.forEach((setName, set) -> addWoodSetTags(
                ModTags.ItemTags.woodSetLogs(setName),
                set.log(),
                set.wood(),
                set.strippedLog(),
                set.strippedWood(),
                set.planks(),
                set.leaves(),
                set.slab(),
                set.stairs(),
                set.fence(),
                set.fenceGate(),
                set.door(),
                set.trapdoor(),
                set.button(),
                set.pressurePlate(),
                ModItems.WOOD_SET_SIGNS.get(setName).get(),
                ModItems.WOOD_SET_HANGING_SIGNS.get(setName).get(),
                ModItems.WOOD_SET_BOATS.get(setName).get(),
                ModItems.WOOD_SET_CHEST_BOATS.get(setName).get()
        ));

        // --- 6. INDIVIDUALS ---
        this.tag(net.minecraft.tags.ItemTags.TRIM_MATERIALS).add(ModItems.RESIN_BRICK.get());

        this.tag(ModTags.ItemTags.C_CHAINS).add(Items.CHAIN);
        ModBlocks.COPPER_CHAINS.values().forEach(supplier -> this.tag(ModTags.ItemTags.C_CHAINS).add(supplier.get().asItem()));

        this.tag(ModTags.ItemTags.C_NUGGETS).addTag(ModTags.ItemTags.C_NUGGETS_COPPER);
        this.tag(ModTags.ItemTags.C_NUGGETS_COPPER).add(ModItems.COPPER_NUGGET.get());
        this.tag(ModTags.ItemTags.C_COPPER_NUGGETS).add(ModItems.COPPER_NUGGET.get());

        this.tag(ModTags.ItemTags.C_TOOLS_MELEE_WEAPON).add(
                ModItems.COPPER_SWORD.get(), ModItems.EXPOSED_COPPER_SWORD.get(), ModItems.WEATHERED_COPPER_SWORD.get(), ModItems.OXIDIZED_COPPER_SWORD.get()
        );
        this.tag(ModTags.ItemTags.C_TOOLS_MINING_TOOL).add(
                ModItems.COPPER_PICKAXE.get(), ModItems.EXPOSED_COPPER_PICKAXE.get(), ModItems.WEATHERED_COPPER_PICKAXE.get(), ModItems.OXIDIZED_COPPER_PICKAXE.get(),
                ModItems.COPPER_SHOVEL.get(), ModItems.EXPOSED_COPPER_SHOVEL.get(), ModItems.WEATHERED_COPPER_SHOVEL.get(), ModItems.OXIDIZED_COPPER_SHOVEL.get(),
                ModItems.COPPER_AXE.get(), ModItems.EXPOSED_COPPER_AXE.get(), ModItems.WEATHERED_COPPER_AXE.get(), ModItems.OXIDIZED_COPPER_AXE.get(),
                ModItems.COPPER_HOE.get(), ModItems.EXPOSED_COPPER_HOE.get(), ModItems.WEATHERED_COPPER_HOE.get(), ModItems.OXIDIZED_COPPER_HOE.get()
        );
        this.tag(ModTags.ItemTags.C_TOOLS_SHEAR).add(
                ModItems.COPPER_SHEARS.get(), ModItems.EXPOSED_COPPER_SHEARS.get(), ModItems.WEATHERED_COPPER_SHEARS.get(), ModItems.OXIDIZED_COPPER_SHEARS.get()
        );

        this.tag(ModTags.ItemTags.LANTERNS).add(Items.LANTERN, Items.SOUL_LANTERN);
        ModBlocks.COPPER_LANTERNS.values().forEach(supplier -> this.tag(ModTags.ItemTags.LANTERNS).add(supplier.get().asItem()));
        ModBlocks.COPPER_SOUL_LANTERNS.values().forEach(supplier -> this.tag(ModTags.ItemTags.LANTERNS).add(supplier.get().asItem()));

        this.tag(ModTags.ItemTags.LIGHTNING_RODS).add(Items.LIGHTNING_ROD);
        ModBlocks.LIGHTNING_RODS.values().forEach(supplier -> this.tag(ModTags.ItemTags.LIGHTNING_RODS).add(supplier.get().asItem()));

        this.tag(ModTags.ItemTags.RAILS).add(Items.RAIL, Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL);
        ModBlocks.COPPER_RAILS.values().forEach(supplier -> this.tag(ModTags.ItemTags.RAILS).add(supplier.get().asItem()));

        this.tag(ModTags.ItemTags.ANVIL).add(Items.ANVIL, Items.CHIPPED_ANVIL, Items.DAMAGED_ANVIL);
        ModBlocks.COPPER_ANVILS.values().forEach(supplier -> this.tag(ModTags.ItemTags.ANVIL).add(supplier.get().asItem()));

        this.tag(net.minecraft.tags.ItemTags.COALS).add(ModItems.TINY_COAL.get(), ModItems.TINY_CHARCOAL.get());

        // --- 7. DYEABLE ITEMS ---
        var dyeableBanners = this.tag(ModTags.ItemTags.DYEABLE_BANNERS);
        var dyeableCandles = this.tag(ModTags.ItemTags.DYEABLE_CANDLES);
        var dyeableGlassBlocks = this.tag(ModTags.ItemTags.DYEABLE_GLASS_BLOCKS);
        var dyeableGlassPanes = this.tag(ModTags.ItemTags.DYEABLE_GLASS_PANES);
        var dyeableShulkerBoxes = this.tag(ModTags.ItemTags.DYEABLE_SHULKER_BOXES);
        var dyeableConcrete = this.tag(ModTags.ItemTags.DYEABLE_CONCRETE);
        var dyeableConcretePowder = this.tag(ModTags.ItemTags.DYEABLE_CONCRETE_POWDER);
        var dyeableTerracotta = this.tag(ModTags.ItemTags.DYEABLE_TERRACOTTA);

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

        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            var blocks = ModBlocks.COLOR_SETS.get(name);
            dyeableBanners.add(blocks.banner().get().asItem());
            dyeableCandles.add(blocks.candle().get().asItem());
            dyeableGlassBlocks.add(blocks.stainedGlass().get().asItem());
            dyeableGlassPanes.add(blocks.stainedGlassPane().get().asItem());
            dyeableShulkerBoxes.add(blocks.shulkerBox().get().asItem());
            dyeableConcrete.add(blocks.concrete().get().asItem());
            dyeableConcretePowder.add(blocks.concretePowder().get().asItem());
            dyeableTerracotta.add(blocks.terracotta().get().asItem());
        }
        dyeableCandles.addOptional(ResourceLocation.withDefaultNamespace("candle"));
        dyeableGlassBlocks.addOptional(ResourceLocation.withDefaultNamespace("glass"));
        dyeableGlassPanes.addOptional(ResourceLocation.withDefaultNamespace("glass_pane"));
        dyeableShulkerBoxes.addOptional(ResourceLocation.withDefaultNamespace("shulker_box"));
        dyeableTerracotta.addOptional(ResourceLocation.withDefaultNamespace("terracotta"));

        this.tag(ModTags.ItemTags.HAPPY_GHAST_TEMPT_ITEMS).add(Items.CHERRY_SAPLING, Items.MANGROVE_PROPAGULE, Items.SNOWBALL);
        this.tag(ModTags.ItemTags.HAPPY_GHAST_FOOD).add(Items.CHERRY_SAPLING, Items.MANGROVE_PROPAGULE, Items.SNOWBALL);
        this.tag(ModTags.ItemTags.HARNESSES).add(ModItems.HARNESSES.values().stream().map(DeferredItem::get).toArray(Item[]::new));
        this.tag(ModTags.ItemTags.BUNDLES).add(Items.BUNDLE).add(ModItems.BUNDLES.values().stream().map(DeferredItem::get).toArray(Item[]::new));

        this.tag(ModTags.ItemTags.INVENTORY_OPENABLE)
                .addTag(ModTags.ItemTags.DYEABLE_SHULKER_BOXES)
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

        this.tag(net.minecraft.tags.ItemTags.ARROWS).add(Items.ARROW, Items.TIPPED_ARROW, Items.SPECTRAL_ARROW, ModItems.TORCH_ARROW.get());

        this.tag(net.minecraft.tags.ItemTags.SWORDS).add(
                ModItems.COPPER_SWORD.get(), ModItems.EXPOSED_COPPER_SWORD.get(), ModItems.WEATHERED_COPPER_SWORD.get(), ModItems.OXIDIZED_COPPER_SWORD.get()
        );
        this.tag(net.minecraft.tags.ItemTags.SHOVELS).add(
                ModItems.COPPER_SHOVEL.get(), ModItems.EXPOSED_COPPER_SHOVEL.get(), ModItems.WEATHERED_COPPER_SHOVEL.get(), ModItems.OXIDIZED_COPPER_SHOVEL.get()
        );
        this.tag(net.minecraft.tags.ItemTags.PICKAXES).add(
                ModItems.COPPER_PICKAXE.get(), ModItems.EXPOSED_COPPER_PICKAXE.get(), ModItems.WEATHERED_COPPER_PICKAXE.get(), ModItems.OXIDIZED_COPPER_PICKAXE.get()
        );
        this.tag(net.minecraft.tags.ItemTags.AXES).add(
                ModItems.COPPER_AXE.get(), ModItems.EXPOSED_COPPER_AXE.get(), ModItems.WEATHERED_COPPER_AXE.get(), ModItems.OXIDIZED_COPPER_AXE.get()
        );
        this.tag(net.minecraft.tags.ItemTags.HOES).add(
                ModItems.COPPER_HOE.get(), ModItems.EXPOSED_COPPER_HOE.get(), ModItems.WEATHERED_COPPER_HOE.get(), ModItems.OXIDIZED_COPPER_HOE.get()
        );

        this.tag(net.minecraft.tags.ItemTags.HEAD_ARMOR).add(
                ModItems.COPPER_HELMET.get(), ModItems.EXPOSED_COPPER_HELMET.get(), ModItems.WEATHERED_COPPER_HELMET.get(), ModItems.OXIDIZED_COPPER_HELMET.get(),
                ModItems.COPPER_CHAINMAIL_HELMET.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_HELMET.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_HELMET.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_HELMET.get()
        );
        this.tag(net.minecraft.tags.ItemTags.CHEST_ARMOR).add(
                ModItems.COPPER_CHESTPLATE.get(), ModItems.EXPOSED_COPPER_CHESTPLATE.get(), ModItems.WEATHERED_COPPER_CHESTPLATE.get(), ModItems.OXIDIZED_COPPER_CHESTPLATE.get(),
                ModItems.COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_CHESTPLATE.get()
        );
        this.tag(net.minecraft.tags.ItemTags.LEG_ARMOR).add(
                ModItems.COPPER_LEGGINGS.get(), ModItems.EXPOSED_COPPER_LEGGINGS.get(), ModItems.WEATHERED_COPPER_LEGGINGS.get(), ModItems.OXIDIZED_COPPER_LEGGINGS.get(),
                ModItems.COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_LEGGINGS.get()
        );
        this.tag(net.minecraft.tags.ItemTags.FOOT_ARMOR).add(
                ModItems.COPPER_BOOTS.get(), ModItems.EXPOSED_COPPER_BOOTS.get(), ModItems.WEATHERED_COPPER_BOOTS.get(), ModItems.OXIDIZED_COPPER_BOOTS.get(),
                ModItems.COPPER_CHAINMAIL_BOOTS.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_BOOTS.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_BOOTS.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_BOOTS.get()
        );

        this.tag(net.minecraft.tags.ItemTags.TRIMMABLE_ARMOR).add(
                ModItems.COPPER_HELMET.get(), ModItems.COPPER_CHESTPLATE.get(), ModItems.COPPER_LEGGINGS.get(), ModItems.COPPER_BOOTS.get(),
                ModItems.EXPOSED_COPPER_HELMET.get(), ModItems.EXPOSED_COPPER_CHESTPLATE.get(), ModItems.EXPOSED_COPPER_LEGGINGS.get(), ModItems.EXPOSED_COPPER_BOOTS.get(),
                ModItems.WEATHERED_COPPER_HELMET.get(), ModItems.WEATHERED_COPPER_CHESTPLATE.get(), ModItems.WEATHERED_COPPER_LEGGINGS.get(), ModItems.WEATHERED_COPPER_BOOTS.get(),
                ModItems.OXIDIZED_COPPER_HELMET.get(), ModItems.OXIDIZED_COPPER_CHESTPLATE.get(), ModItems.OXIDIZED_COPPER_LEGGINGS.get(), ModItems.OXIDIZED_COPPER_BOOTS.get(),
                ModItems.COPPER_CHAINMAIL_HELMET.get(), ModItems.COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.COPPER_CHAINMAIL_BOOTS.get(),
                ModItems.EXPOSED_COPPER_CHAINMAIL_HELMET.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.EXPOSED_COPPER_CHAINMAIL_BOOTS.get(),
                ModItems.WEATHERED_COPPER_CHAINMAIL_HELMET.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.WEATHERED_COPPER_CHAINMAIL_BOOTS.get(),
                ModItems.OXIDIZED_COPPER_CHAINMAIL_HELMET.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_CHESTPLATE.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_LEGGINGS.get(), ModItems.OXIDIZED_COPPER_CHAINMAIL_BOOTS.get()
        );
        this.tag(ModTags.ItemTags.STRIPPED_PALE_OAK_LOG).add(ModBlocks.STRIPPED_PALE_OAK_LOG.get().asItem())
                .addOptional(ResourceLocation.fromNamespaceAndPath("vanillabackport", "stripped_pale_oak_log"));

        // Fish Tags
        this.tag(ModTags.ItemTags.C_RAW_FISH)
                .add(ModItems.BASS.get(), ModItems.CATFISH.get(), ModItems.KOI_FISH.get(), ModItems.RAW_ANGELFISH.get(), ModItems.RAW_BARRELEYE.get(), ModItems.RAW_FLOUNDER.get(), ModItems.RAW_KRILL.get(), ModItems.RAW_SHRIMP_1.get(), ModItems.RAW_SUNFISH_MEAT.get(), ModItems.RAW_GOLDEN_SUNFISH_MEAT.get());

        this.tag(ModTags.ItemTags.NF_RAW_FISHES)
                .add(ModItems.BASS.get(), ModItems.CATFISH.get(), ModItems.KOI_FISH.get(), ModItems.RAW_ANGELFISH.get(), ModItems.RAW_BARRELEYE.get(), ModItems.RAW_FLOUNDER.get(), ModItems.RAW_KRILL.get(), ModItems.RAW_SHRIMP_1.get(), ModItems.RAW_SUNFISH_MEAT.get(), ModItems.RAW_GOLDEN_SUNFISH_MEAT.get());

        this.tag(ModTags.ItemTags.C_COOKED_FISH)
                .add(ModItems.COOKED_BASS.get(), ModItems.COOKED_CATFISH.get(), ModItems.FRIED_KRILL.get(), ModItems.COOKED_SHRIMP_1.get(), ModItems.COOKED_SUNFISH_MEAT.get(), ModItems.COOKED_GOLDEN_SUNFISH_MEAT.get());

        this.tag(ModTags.ItemTags.NF_COOKED_FISHES)
                .add(ModItems.COOKED_BASS.get(), ModItems.COOKED_CATFISH.get(), ModItems.FRIED_KRILL.get(), ModItems.COOKED_SHRIMP_1.get(), ModItems.COOKED_SUNFISH_MEAT.get(), ModItems.COOKED_GOLDEN_SUNFISH_MEAT.get());

        // Meat Tags
        this.tag(ModTags.ItemTags.C_RAW_MEAT)
                .add(ModItems.VENISON.get(), ModItems.PHEASANT.get(), ModItems.BUSHMEAT.get(), ModItems.LIZARD_TAIL.get(), ModItems.RAW_CAPYBARA.get(), ModItems.RAW_HEDGEHOG.get(), ModItems.RAW_SEAL.get(), ModItems.RAW_KIWI.get());

        this.tag(ModTags.ItemTags.C_COOKED_MEAT)
                .add(ModItems.COOKED_VENISON.get(), ModItems.COOKED_PHEASANT.get(), ModItems.COOKED_BUSHMEAT.get(), ModItems.COOKED_LIZARD_TAIL.get(), ModItems.COOKED_CAPYBARA.get(), ModItems.COOKED_HEDGEHOG.get(), ModItems.COOKED_SEAL.get(), ModItems.COOKED_KIWI.get());

        this.tag(ModTags.ItemTags.IS_MEAT)
                .addTag(ModTags.ItemTags.C_RAW_MEAT)
                .addTag(ModTags.ItemTags.C_COOKED_MEAT);

        // Egg Tags
        this.tag(ModTags.ItemTags.C_EGGS)
                .add(Items.EGG, ModItems.BLUE_EGG.get(), ModItems.BROWN_EGG.get(), ModItems.DUCK_EGG.get(), ModItems.PHEASANT_EGG.get(), ModItems.ALLIGATOR_EGG.get(), ModItems.TORTOISE_EGG.get(), ModItems.SNAIL_EGG.get(), ModItems.KIWI_EGG.get(), ModItems.PENGUIN_EGG.get(), ModItems.EMU_EGG.get(), ModItems.HOOPOE_EGG.get(), ModItems.TOUCAN_EGG.get());

        this.tag(ModTags.ItemTags.NF_EGGS)
                .add(Items.EGG, ModItems.BLUE_EGG.get(), ModItems.BROWN_EGG.get(), ModItems.DUCK_EGG.get(), ModItems.PHEASANT_EGG.get(), ModItems.ALLIGATOR_EGG.get(), ModItems.TORTOISE_EGG.get(), ModItems.SNAIL_EGG.get(), ModItems.KIWI_EGG.get(), ModItems.PENGUIN_EGG.get(), ModItems.EMU_EGG.get(), ModItems.HOOPOE_EGG.get(), ModItems.TOUCAN_EGG.get());

        this.tag(ModTags.ItemTags.C_COOKED_EGGS)
                .add(ModItems.COOKED_EGG.get());

        this.tag(ModTags.ItemTags.NF_COOKED_EGGS)
                .add(ModItems.COOKED_EGG.get());
    }

    @SafeVarargs
    private void addCommonLinkageTags(TagAppender<Item> appender, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            appender.addTag(tag);
        }
    }

    private void copyCommonTag(TagKey<Item> itemTag) {
        this.copy(TagKey.create(Registries.BLOCK, itemTag.location()), itemTag);
    }

    private void addWoodSetTags(TagKey<Item> logTag, ItemLike log, ItemLike wood, ItemLike strippedLog, ItemLike strippedWood,
                                ItemLike planks, ItemLike leaves, ItemLike slab, ItemLike stairs, ItemLike fence, ItemLike fenceGate,
                                ItemLike door, ItemLike trapdoor, ItemLike button, ItemLike pressurePlate, ItemLike sign,
                                ItemLike hangingSign, ItemLike boat, ItemLike chestBoat) {
        this.tag(logTag).add(log.asItem(), wood.asItem(), strippedLog.asItem(), strippedWood.asItem());
        this.tag(net.minecraft.tags.ItemTags.LOGS).addTag(logTag);
        this.tag(net.minecraft.tags.ItemTags.LOGS_THAT_BURN).addTag(logTag);
        this.tag(net.minecraft.tags.ItemTags.PLANKS).add(planks.asItem());
        this.tag(net.minecraft.tags.ItemTags.LEAVES).add(leaves.asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_SLABS).add(slab.asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_STAIRS).add(stairs.asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_FENCES).add(fence.asItem());
        this.tag(net.minecraft.tags.ItemTags.FENCE_GATES).add(fenceGate.asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_DOORS).add(door.asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_TRAPDOORS).add(trapdoor.asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_BUTTONS).add(button.asItem());
        this.tag(net.minecraft.tags.ItemTags.WOODEN_PRESSURE_PLATES).add(pressurePlate.asItem());
        this.tag(net.minecraft.tags.ItemTags.SIGNS).add(sign.asItem());
        this.tag(net.minecraft.tags.ItemTags.HANGING_SIGNS).add(hangingSign.asItem());
        this.tag(net.minecraft.tags.ItemTags.BOATS).add(boat.asItem());
        this.tag(net.minecraft.tags.ItemTags.CHEST_BOATS).add(chestBoat.asItem());
    }
}