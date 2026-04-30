package com.otterly76.ott.generation;

import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import com.otterly76.ott.Constants;
import com.otterly76.ott.color.ModColorSets;
import com.otterly76.ott.block.IGradientBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.util.ModTags;
import com.otterly76.ott.recipe.BundleColoring;
import com.otterly76.ott.recipe.EngravingRecipe;
import com.otterly76.ott.recipe.WoodcuttingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    private static Criterion<?> impossible() {
        return new Criterion<>(CriteriaTriggers.IMPOSSIBLE, new ImpossibleTrigger.TriggerInstance());
    }

    private record NoAdvancementOutput(RecipeOutput delegate) implements RecipeOutput {

        @Override
            public void accept(@NotNull ResourceLocation id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition @NotNull ... conditions) {
                delegate.accept(id, recipe, null, conditions);
            }

            @Override
            public Advancement.@NotNull Builder advancement() {
                return Advancement.Builder.recipeAdvancement();
            }
        }

    private ResourceLocation getRecipePath(String namespace, String recipeName) {
        return ResourceLocation.fromNamespaceAndPath(namespace, recipeName);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput exporter) {
        RecipeOutput noAdv = new NoAdvancementOutput(exporter);

        // Wood (backported pale oak + ott wood sets)
        this.woodRecipes(noAdv);

        // Copper (backported items and blocks)
        this.copperRecipes(noAdv);
        this.copperToolArmorRecipes(noAdv);

        // Shelves
        this.shelfRecipes(noAdv);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.NAME_TAG)
                .define('P', Items.PAPER)
                .define('S', Items.STRING)
                .pattern("P")
                .pattern("P")
                .pattern("S")
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(noAdv, getRecipePath(Constants.MOD_ID, "nametag"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WOODCUTTER.get())
                .define('L', net.minecraft.tags.ItemTags.LOGS)
                .define('I', Items.IRON_INGOT)
                .pattern("   ")
                .pattern(" I ")
                .pattern("LLL")
                .unlockedBy("has_logs", has(net.minecraft.tags.ItemTags.LOGS))
                .save(noAdv, getRecipePath(Constants.MOD_ID, "woodcutter"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENGRAVING_TABLE.get())
                .define('I', Items.IRON_INGOT)
                .define('S', Blocks.SMOOTH_STONE_SLAB)
                .define('C', Blocks.STONE)
                .pattern(" I ")
                .pattern("SSS")
                .pattern("C C")
                .unlockedBy("has_stone", has(Blocks.STONE))
                .save(noAdv, getRecipePath(Constants.MOD_ID, "engraving_table"));

        this.engraveRecipes(noAdv);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GLASS_JAR.get())
                .define('G', Items.GLASS_PANE)
                .pattern("G G")
                .pattern("G G")
                .pattern("GGG")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath(Constants.MOD_ID, "glass_jar"));

        // Ott Critters
        this.ottCrittersRecipes(noAdv);

        // Custom Dyes
        this.addCustomDyeRecipes(noAdv);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GRAY_DYE)
                .requires(ModBlocks.CLOSED_EYEBLOSSOM.get())
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "gray_dye_from_closed_eyeblossom"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ORANGE_DYE)
                .requires(ModBlocks.OPEN_EYEBLOSSOM.get())
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "orange_dye_from_open_eyeblossom"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.BEEHIVE)
                .define('P', Items.OAK_PLANKS)
                .define('H', Items.HONEYCOMB)
                .pattern("PPP")
                .pattern("HHH")
                .pattern("PPP")
                .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
                .save(noAdv, getRecipePath("minecraft", "beehive"));

        // Custom wood beehive recipes
        java.util.Map<String, net.minecraft.world.item.Item> beehivePlanks = java.util.Map.ofEntries(
                java.util.Map.entry("acacia",   Items.ACACIA_PLANKS),
                java.util.Map.entry("bamboo",   Items.BAMBOO_PLANKS),
                java.util.Map.entry("birch",    Items.BIRCH_PLANKS),
                java.util.Map.entry("cherry",   Items.CHERRY_PLANKS),
                java.util.Map.entry("crimson",  Items.CRIMSON_PLANKS),
                java.util.Map.entry("dark_oak", Items.DARK_OAK_PLANKS),
                java.util.Map.entry("jungle",   Items.JUNGLE_PLANKS),
                java.util.Map.entry("mangrove", Items.MANGROVE_PLANKS),
                java.util.Map.entry("pale_oak", ModBlocks.PALE_OAK_PLANKS.get().asItem()),
                java.util.Map.entry("spruce",   Items.SPRUCE_PLANKS),
                java.util.Map.entry("warped",   Items.WARPED_PLANKS)
        );
        java.util.Map<String, net.minecraft.world.level.block.Block> beehiveBlocks = java.util.Map.ofEntries(
                java.util.Map.entry("acacia",   ModBlocks.ACACIA_BEEHIVE.get()),
                java.util.Map.entry("bamboo",   ModBlocks.BAMBOO_BEEHIVE.get()),
                java.util.Map.entry("birch",    ModBlocks.BIRCH_BEEHIVE.get()),
                java.util.Map.entry("cherry",   ModBlocks.CHERRY_BEEHIVE.get()),
                java.util.Map.entry("crimson",  ModBlocks.CRIMSON_BEEHIVE.get()),
                java.util.Map.entry("dark_oak", ModBlocks.DARK_OAK_BEEHIVE.get()),
                java.util.Map.entry("jungle",   ModBlocks.JUNGLE_BEEHIVE.get()),
                java.util.Map.entry("mangrove", ModBlocks.MANGROVE_BEEHIVE.get()),
                java.util.Map.entry("pale_oak", ModBlocks.PALE_OAK_BEEHIVE.get()),
                java.util.Map.entry("spruce",   ModBlocks.SPRUCE_BEEHIVE.get()),
                java.util.Map.entry("warped",   ModBlocks.WARPED_BEEHIVE.get())
        );
        beehivePlanks.forEach((wood, planks) -> {
            net.minecraft.world.level.block.Block result = beehiveBlocks.get(wood);
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result)
                    .define('P', planks)
                    .define('H', Items.HONEYCOMB)
                    .pattern("PPP")
                    .pattern("HHH")
                    .pattern("PPP")
                    .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
                    .save(noAdv, getRecipePath("ott", wood + "_beehive"));
        });
        // OTT wood set beehives
        ModBlocks.WOOD_SETS.forEach((wood, set) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, set.beehive().get())
                        .define('P', set.planks().get())
                        .define('H', Items.HONEYCOMB)
                        .pattern("PPP")
                        .pattern("HHH")
                        .pattern("PPP")
                        .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
                        .save(exporter, getRecipePath("ott", wood + "_beehive")));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CREAKING_HEART.get())
                .define('#', ModBlocks.PALE_OAK_LOG.get())
                .define('O', ModBlocks.RESIN_BLOCK.get())
                .pattern("#")
                .pattern("O")
                .pattern("#")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "creaking_heart"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(Items.SOUL_SAND),
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.SOUL_GLASS.get(),
                        0.1F,
                        200
                )
                .unlockedBy("has_soul_sand", has(Items.SOUL_SAND))
                .save(exporter, getRecipePath("ott", "soul_glass_from_smelting"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.RESIN_CLUMP.get()),
                        RecipeCategory.MISC,
                        ModItems.RESIN_BRICK.get(),
                        0.1F,
                        200
                )
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_brick"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BLOCK.get())
                .define('#', ModBlocks.RESIN_CLUMP.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICKS.get())
                .define('#', ModItems.RESIN_BRICK.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_STAIRS.get(), 4)
                .define('#', ModBlocks.RESIN_BRICKS.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_brick_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_SLAB.get(), 6)
                .define('#', ModBlocks.RESIN_BRICKS.get())
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_brick_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_WALL.get(), 6)
                .define('#', ModBlocks.RESIN_BRICKS.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_brick_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RESIN_BRICKS.get())
                .define('#', ModBlocks.RESIN_BRICK_SLAB.get())
                .pattern("#")
                .pattern("#")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "chiseled_resin_bricks"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.RESIN_CLUMP.get(), 9)
                .requires(ModBlocks.RESIN_BLOCK.get())
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_clump_from_resin_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PALE_MOSS_CARPET.get(), 3)
                .define('#', ModBlocks.PALE_MOSS_BLOCK.get())
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_moss_carpet"));

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(ModBlocks.RESIN_BRICKS.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.RESIN_BRICK_SLAB.get(),
                        2
                )
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_brick_slab_from_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(ModBlocks.RESIN_BRICKS.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.RESIN_BRICK_WALL.get()
                )
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_brick_wall_from_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(ModBlocks.RESIN_BRICKS.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.RESIN_BRICK_STAIRS.get()
                )
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "resin_brick_stairs_from_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(ModBlocks.RESIN_BRICKS.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.CHISELED_RESIN_BRICKS.get()
                )
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "chiseled_resin_bricks_from_stonecutting"));

        ModBlocks.ALL_GRADIENT_BLOCKS.forEach(deferredBlock -> createGradientRecipe(noAdv, deferredBlock.get()));

        SpecialRecipeBuilder.special(BundleColoring::new)
                .save(noAdv, getRecipePath("minecraft", "bundle_coloring"));

        // Tiny Coal and Charcoal
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TINY_COAL.get(), 9)
                .requires(Items.COAL)
                .unlockedBy("has_coal", has(Items.COAL))
                .save(noAdv);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COAL)
                .define('#', ModItems.TINY_COAL.get())
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .unlockedBy("has_tiny_coal", has(ModItems.TINY_COAL.get()))
                .save(noAdv, getRecipePath("ott", "coal_from_tiny_coal"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TINY_CHARCOAL.get(), 9)
                .requires(Items.CHARCOAL)
                .unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(noAdv);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHARCOAL)
                .define('#', ModItems.TINY_CHARCOAL.get())
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .unlockedBy("has_tiny_charcoal", has(ModItems.TINY_CHARCOAL.get()))
                .save(noAdv, getRecipePath("ott", "charcoal_from_tiny_charcoal"));

        // Water Lantern
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.WATER_LANTERN.get())
                .define('#', Items.IRON_INGOT)
                .define('G', Items.GLASS)
                .define('B', Items.WATER_BUCKET)
                .pattern("###")
                .pattern("GBG")
                .pattern("###")
                .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                .save(noAdv);

        // Lava Lantern
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.LAVA_LANTERN.get())
                .define('#', Items.IRON_INGOT)
                .define('G', Items.GLASS)
                .define('B', Items.LAVA_BUCKET)
                .pattern("###")
                .pattern("GBG")
                .pattern("###")
                .unlockedBy("has_lava_bucket", has(Items.LAVA_BUCKET))
                .save(noAdv);

        // Protective Lantern
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PROTECTIVE_LANTERN.get())
                .define('#', Items.GOLD_INGOT)
                .define('G', Items.GLASS)
                .define('B', Items.FIRE_CHARGE)
                .pattern("###")
                .pattern("GBG")
                .pattern("###")
                .unlockedBy("has_fire_charge", has(Items.FIRE_CHARGE))
                .save(noAdv);

        // Smite Lantern
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SMITE_LANTERN.get())
                .define('#', Items.GOLD_INGOT)
                .define('G', Items.GLASS)
                .define('B', Items.MAGMA_CREAM)
                .pattern("###")
                .pattern("GBG")
                .pattern("###")
                .unlockedBy("has_magma_cream", has(Items.MAGMA_CREAM))
                .save(noAdv);

        this.addDyeingRecipes(noAdv);
        this.addPatternBlockRecipes(noAdv);
        this.addElevatorRecipes(noAdv);
        this.addSlabToBlockRecipes(noAdv);
        this.addMiscRecipes(noAdv);
        this.addSeaglassRecipes(noAdv);
        this.addBuildingMaterialRecipes(noAdv);
        this.addStonecutterRecipes(noAdv);

        this.mountsOfMayhemRecipes(noAdv);
    }

    private void addCustomDyeRecipes(RecipeOutput exporter) {
        List<Item> vanillaDyes = List.of(
                Items.WHITE_DYE, Items.PINK_DYE, Items.MAGENTA_DYE, Items.PURPLE_DYE,
                Items.BLUE_DYE, Items.LIGHT_BLUE_DYE, Items.CYAN_DYE, Items.GREEN_DYE,
                Items.LIME_DYE, Items.YELLOW_DYE, Items.ORANGE_DYE, Items.RED_DYE,
                Items.BROWN_DYE, Items.BLACK_DYE, Items.GRAY_DYE, Items.LIGHT_GRAY_DYE
        );

        for (int i = 0; i < ModColorSets.ALL.size(); i++) {
            ModColorSets.ColorSet colorSet = ModColorSets.ALL.get(i);
            if (colorSet.name().equals("navy")) continue; // has its own recipe below
            Item result = ModItems.CUSTOM_DYES.get(colorSet.name()).get();
            Item ingredient1 = vanillaDyes.get(i % vanillaDyes.size());
            Item ingredient2 = vanillaDyes.get((i + 1) % vanillaDyes.size());

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 2)
                    .requires(ingredient1)
                    .requires(ingredient2)
                    .unlockedBy("impossible", impossible())
                    .save(exporter, getRecipePath(Constants.MOD_ID, colorSet.name() + "_dye"));
        }

        // Navy dye: 1 blue dye + 1 charcoal dye → 2 navy dye
        if (ModItems.CUSTOM_DYES.containsKey("navy") && ModItems.CUSTOM_DYES.containsKey("charcoal")) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CUSTOM_DYES.get("navy").get(), 2)
                    .requires(Items.BLUE_DYE)
                    .requires(ModItems.CUSTOM_DYES.get("charcoal").get())
                    .unlockedBy("impossible", impossible())
                    .save(exporter, getRecipePath(Constants.MOD_ID, "navy_dye"));
        }
    }


    private void addSeaglassRecipes(RecipeOutput exporter) {
        ModBlocks.SeaglassColorBlocks lightGray = ModBlocks.SEAGLASS_SETS.get("light_gray");

        // Smelting: dead coral blocks → light_gray seaglass variants
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.DEAD_TUBE_CORAL_BLOCK), RecipeCategory.BUILDING_BLOCKS, lightGray.seaglass().get(), 0.1F, 200)
                .unlockedBy("has_dead_coral", has(Items.DEAD_TUBE_CORAL_BLOCK))
                .save(exporter, getRecipePath("ott", "light_gray_seaglass_from_smelting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.DEAD_BRAIN_CORAL_BLOCK), RecipeCategory.BUILDING_BLOCKS, lightGray.bubblesSeaglass().get(), 0.1F, 200)
                .unlockedBy("has_dead_coral", has(Items.DEAD_BRAIN_CORAL_BLOCK))
                .save(exporter, getRecipePath("ott", "light_gray_bubbles_seaglass_from_smelting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.DEAD_BUBBLE_CORAL_BLOCK), RecipeCategory.BUILDING_BLOCKS, lightGray.smoothSeaglass().get(), 0.1F, 200)
                .unlockedBy("has_dead_coral", has(Items.DEAD_BUBBLE_CORAL_BLOCK))
                .save(exporter, getRecipePath("ott", "light_gray_smooth_seaglass_from_smelting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.DEAD_FIRE_CORAL_BLOCK), RecipeCategory.BUILDING_BLOCKS, lightGray.wavesSeaglass().get(), 0.1F, 200)
                .unlockedBy("has_dead_coral", has(Items.DEAD_FIRE_CORAL_BLOCK))
                .save(exporter, getRecipePath("ott", "light_gray_waves_seaglass_from_smelting"));

        // Dyeing: 8 light_gray seaglass + 1 dye → 8 colored seaglass
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            if (name.equals("light_gray")) continue;
            Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));
            addSeaglassDyeRecipe(exporter, lightGray, ModBlocks.SEAGLASS_SETS.get(name), dye, name);
        }
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item dye = ModItems.CUSTOM_DYES.get(name).get();
            addSeaglassDyeRecipe(exporter, lightGray, ModBlocks.SEAGLASS_SETS.get(name), dye, name);
        }

        // Opal crystal set recipes
        addOpalSetRecipes(exporter, "white_opal", ModBlocks.OPAL_SETS.get("white_opal"), ModItems.WHITE_OPAL_CRYSTAL.get());
        addOpalSetRecipes(exporter, "black_opal", ModBlocks.OPAL_SETS.get("black_opal"), ModItems.BLACK_OPAL_CRYSTAL.get());
        addOpalSetRecipes(exporter, "fire_opal",  ModBlocks.OPAL_SETS.get("fire_opal"),  ModItems.FIRE_OPAL_CRYSTAL.get());
    }

    private void addSeaglassDyeRecipe(RecipeOutput exporter, ModBlocks.SeaglassColorBlocks source, ModBlocks.SeaglassColorBlocks target, Item dye, String colorName) {
        addSeaglassDyeVariant(exporter, source.seaglass().get(),        target.seaglass().get(),        dye, colorName + "_seaglass");
        addSeaglassDyeVariant(exporter, source.bubblesSeaglass().get(),  target.bubblesSeaglass().get(),  dye, colorName + "_bubbles_seaglass");
        addSeaglassDyeVariant(exporter, source.smoothSeaglass().get(),   target.smoothSeaglass().get(),   dye, colorName + "_smooth_seaglass");
        addSeaglassDyeVariant(exporter, source.wavesSeaglass().get(),    target.wavesSeaglass().get(),    dye, colorName + "_waves_seaglass");
    }

    private void addSeaglassDyeVariant(RecipeOutput exporter, Block source, Block result, Item dye, String recipeName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 8)
                .define('#', source)
                .define('D', dye)
                .pattern("###")
                .pattern("#D#")
                .pattern("###")
                .unlockedBy("has_light_gray_seaglass", has(source))
                .save(exporter, getRecipePath("ott", recipeName + "_from_dyeing"));
    }

    private void addBuildingMaterialRecipes(RecipeOutput exporter) {
        // --- Unfired Clay Roof Tile: 8 clay balls + water bucket (ring) → 8 tiles ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UNFIRED_CLAY_ROOF_TILE.get(), 8)
                .define('C', Items.CLAY_BALL)
                .define('W', Items.WATER_BUCKET)
                .pattern("CCC")
                .pattern("CWC")
                .pattern("CCC")
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(exporter, getRecipePath("ott", "unfired_clay_roof_tile"));

        // --- Plaster Bucket: 8 bone meal + water bucket (ring) → 8 plaster buckets ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PLASTER_BUCKET.get(), 8)
                .define('B', Items.BONE_MEAL)
                .define('W', Items.WATER_BUCKET)
                .pattern("BBB")
                .pattern("BWB")
                .pattern("BBB")
                .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                .save(exporter, getRecipePath("ott", "plaster_bucket"));

        // --- Clay tile smelting: unfired → light_gray_clay_tile ---
        Item lightGrayClay = ModItems.CLAY_TILES.get("light_gray").get();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.UNFIRED_CLAY_ROOF_TILE.get()),
                        RecipeCategory.MISC, lightGrayClay, 0.1F, 200)
                .unlockedBy("has_unfired_clay_roof_tile", has(ModItems.UNFIRED_CLAY_ROOF_TILE.get()))
                .save(exporter, getRecipePath("ott", "light_gray_clay_tile_from_smelting"));

        // --- Clay tile dyeing: 8 light_gray + dye → 8 colored (all 32 colors, skip light_gray) ---
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            if (name.equals("light_gray")) continue;
            Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));
            addRingDyeRecipe(exporter, lightGrayClay, ModItems.CLAY_TILES.get(name).get(), dye, name + "_clay_tile");
        }
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item dye = ModItems.CUSTOM_DYES.get(name).get();
            addRingDyeRecipe(exporter, lightGrayClay, ModItems.CLAY_TILES.get(name).get(), dye, name + "_clay_tile");
        }

        // --- Flat roof tiles: 8 {color}_clay_tile (ring) → 8 {color}_flat_roof_tiles (same color) ---
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            Item clay = ModItems.CLAY_TILES.get(name).get();
            Block roof = ModBlocks.PATTERN_BLOCKS.get("flat_roof_tiles").get(name).get();
            addRingShapeRecipe(exporter, clay, roof, 8, name + "_flat_roof_tiles_from_clay");
        }
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item clay = ModItems.CLAY_TILES.get(name).get();
            Block roof = ModBlocks.PATTERN_BLOCKS.get("flat_roof_tiles").get(name).get();
            addRingShapeRecipe(exporter, clay, roof, 8, name + "_flat_roof_tiles_from_clay");
        }

        // --- Layered roof tiles: 6 {color} clay tiles (2 rows) → 6 {color} layered roof tiles (all 32 colors) ---
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            Item clay = ModItems.CLAY_TILES.get(name).get();
            Block layered = ModBlocks.PATTERN_BLOCKS.get("layered_roof_tiles").get(name).get();
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, layered, 6)
                    .define('C', clay)
                    .pattern("CCC")
                    .pattern("CCC")
                    .unlockedBy("has_clay_tile", has(clay))
                    .save(exporter, getRecipePath("ott", name + "_layered_roof_tiles"));
        }
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item clay = ModItems.CLAY_TILES.get(name).get();
            Block layered = ModBlocks.PATTERN_BLOCKS.get("layered_roof_tiles").get(name).get();
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, layered, 6)
                    .define('C', clay)
                    .pattern("CCC")
                    .pattern("CCC")
                    .unlockedBy("has_clay_tile", has(clay))
                    .save(exporter, getRecipePath("ott", name + "_layered_roof_tiles"));
        }

        // --- Plastered stone: 8 stone (ring) + plaster_bucket → 8 light_gray_plastered_stone ---
        Block lightGrayPlastered = ModBlocks.PATTERN_BLOCKS.get("plastered_stone").get("light_gray").get();
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, lightGrayPlastered, 8)
                .define('S', Items.STONE)
                .define('P', ModItems.PLASTER_BUCKET.get())
                .pattern("SSS")
                .pattern("SPS")
                .pattern("SSS")
                .unlockedBy("has_plaster", has(ModItems.PLASTER_BUCKET.get()))
                .save(exporter, getRecipePath("ott", "light_gray_plastered_stone"));

        // --- Plastered stone dyeing: 8 light_gray + dye → 8 colored (all 32 colors, skip light_gray) ---
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            if (name.equals("light_gray")) continue;
            Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));
            addRingDyeRecipe(exporter, lightGrayPlastered, ModBlocks.PATTERN_BLOCKS.get("plastered_stone").get(name).get(), dye, name + "_plastered_stone");
        }
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item dye = ModItems.CUSTOM_DYES.get(name).get();
            addRingDyeRecipe(exporter, lightGrayPlastered, ModBlocks.PATTERN_BLOCKS.get("plastered_stone").get(name).get(), dye, name + "_plastered_stone");
        }

        // --- Chiseled plastered stone variants: white + dye → colored (1:1 and 8:8, skip white) ---
        for (String pattern : List.of(
                "chiseled_plastered_stone",
                "gilded_plastered_stone",
                "delicate_plastered_stone",
                "banded_plastered_stone")) {
            Block whiteBase = ModBlocks.PATTERN_BLOCKS.get(pattern).get("white").get();
            for (DyeColor color : DyeColor.values()) {
                String name = color.getName();
                if (name.equals("white")) continue;
                Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));
                Block result = ModBlocks.PATTERN_BLOCKS.get(pattern).get(name).get();
                addRingDyeRecipe(exporter, whiteBase, result, dye, name + "_" + pattern);
                addSingleDyeRecipe(exporter, whiteBase, result, dye, name + "_" + pattern);
            }
            for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
                String name = colorSet.name();
                Item dye = ModItems.CUSTOM_DYES.get(name).get();
                Block result = ModBlocks.PATTERN_BLOCKS.get(pattern).get(name).get();
                addRingDyeRecipe(exporter, whiteBase, result, dye, name + "_" + pattern);
                addSingleDyeRecipe(exporter, whiteBase, result, dye, name + "_" + pattern);
            }
        }

        // --- Wheat thatch: 6 wheat (2 rows) → 1 wheat_thatch ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WHEAT_THATCH.get())
                .define('W', Items.WHEAT)
                .pattern("WWW")
                .pattern("WWW")
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(exporter, getRecipePath("ott", "wheat_thatch"));

        // --- Bamboo thatch: 6 bamboo (2 rows) → 1 bamboo_thatch ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BAMBOO_THATCH.get())
                .define('B', Items.BAMBOO)
                .pattern("BBB")
                .pattern("BBB")
                .unlockedBy("has_bamboo", has(Items.BAMBOO))
                .save(exporter, getRecipePath("ott", "bamboo_thatch"));

        // --- Cobbled limestone smelting → limestone ---
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.COBBLED_LIMESTONE.get()),
                        RecipeCategory.BUILDING_BLOCKS, ModBlocks.PLAIN_LIMESTONE.get(), 0.1F, 200)
                .unlockedBy("has_cobbled_limestone", has(ModBlocks.COBBLED_LIMESTONE.get()))
                .save(exporter, getRecipePath("ott", "limestone_from_cobbled_limestone_smelting"));

        // --- Ornamented red wool: red_wool center + 4 gold nuggets at corners → 8 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORNAMENTED_RED_WOOL.get(), 8)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.RED_WOOL)
                .pattern("N N")
                .pattern("NRN")
                .pattern("N N")
                .unlockedBy("has_red_wool", has(Items.RED_WOOL))
                .save(exporter, getRecipePath("ott", "ornamented_red_wool"));

        // --- Delicate red wool: red_wool center + 4 gold nuggets in + shape → 8 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DELICATE_RED_WOOL.get(), 8)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.RED_WOOL)
                .pattern(" N ")
                .pattern("NRN")
                .pattern(" N ")
                .unlockedBy("has_red_wool", has(Items.RED_WOOL))
                .save(exporter, getRecipePath("ott", "delicate_red_wool"));

        // --- Ornamented / Delicate red carpet: 2 wool → 3 carpet ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.ORNAMENTED_RED_CARPET.get(), 3)
                .define('W', ModBlocks.ORNAMENTED_RED_WOOL.get())
                .pattern("WW")
                .unlockedBy("has_ornamented_red_wool", has(ModBlocks.ORNAMENTED_RED_WOOL.get()))
                .save(exporter, getRecipePath("ott", "ornamented_red_carpet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.DELICATE_RED_CARPET.get(), 3)
                .define('W', ModBlocks.DELICATE_RED_WOOL.get())
                .pattern("WW")
                .unlockedBy("has_delicate_red_wool", has(ModBlocks.DELICATE_RED_WOOL.get()))
                .save(exporter, getRecipePath("ott", "delicate_red_carpet"));

        // --- Ornamented blue wool ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORNAMENTED_BLUE_WOOL.get(), 8)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.BLUE_WOOL)
                .pattern("N N")
                .pattern("NRN")
                .pattern("N N")
                .unlockedBy("has_blue_wool", has(Items.BLUE_WOOL))
                .save(exporter, getRecipePath("ott", "ornamented_blue_wool"));

        // --- Delicate blue wool ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DELICATE_BLUE_WOOL.get(), 8)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.BLUE_WOOL)
                .pattern(" N ")
                .pattern("NRN")
                .pattern(" N ")
                .unlockedBy("has_blue_wool", has(Items.BLUE_WOOL))
                .save(exporter, getRecipePath("ott", "delicate_blue_wool"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.ORNAMENTED_BLUE_CARPET.get(), 3)
                .define('W', ModBlocks.ORNAMENTED_BLUE_WOOL.get())
                .pattern("WW")
                .unlockedBy("has_ornamented_blue_wool", has(ModBlocks.ORNAMENTED_BLUE_WOOL.get()))
                .save(exporter, getRecipePath("ott", "ornamented_blue_carpet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.DELICATE_BLUE_CARPET.get(), 3)
                .define('W', ModBlocks.DELICATE_BLUE_WOOL.get())
                .pattern("WW")
                .unlockedBy("has_delicate_blue_wool", has(ModBlocks.DELICATE_BLUE_WOOL.get()))
                .save(exporter, getRecipePath("ott", "delicate_blue_carpet"));

        // --- Ornamented green wool ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORNAMENTED_GREEN_WOOL.get(), 8)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.GREEN_WOOL)
                .pattern("N N")
                .pattern("NRN")
                .pattern("N N")
                .unlockedBy("has_green_wool", has(Items.GREEN_WOOL))
                .save(exporter, getRecipePath("ott", "ornamented_green_wool"));

        // --- Delicate green wool ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DELICATE_GREEN_WOOL.get(), 8)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.GREEN_WOOL)
                .pattern(" N ")
                .pattern("NRN")
                .pattern(" N ")
                .unlockedBy("has_green_wool", has(Items.GREEN_WOOL))
                .save(exporter, getRecipePath("ott", "delicate_green_wool"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.ORNAMENTED_GREEN_CARPET.get(), 3)
                .define('W', ModBlocks.ORNAMENTED_GREEN_WOOL.get())
                .pattern("WW")
                .unlockedBy("has_ornamented_green_wool", has(ModBlocks.ORNAMENTED_GREEN_WOOL.get()))
                .save(exporter, getRecipePath("ott", "ornamented_green_carpet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.DELICATE_GREEN_CARPET.get(), 3)
                .define('W', ModBlocks.DELICATE_GREEN_WOOL.get())
                .pattern("WW")
                .unlockedBy("has_delicate_green_wool", has(ModBlocks.DELICATE_GREEN_WOOL.get()))
                .save(exporter, getRecipePath("ott", "delicate_green_carpet"));

        // --- Ornamented purple wool ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORNAMENTED_PURPLE_WOOL.get(), 8)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.PURPLE_WOOL)
                .pattern("N N")
                .pattern("NRN")
                .pattern("N N")
                .unlockedBy("has_purple_wool", has(Items.PURPLE_WOOL))
                .save(exporter, getRecipePath("ott", "ornamented_purple_wool"));

        // --- Delicate purple wool ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DELICATE_PURPLE_WOOL.get(), 8)
                .define('N', Items.GOLD_NUGGET)
                .define('R', Items.PURPLE_WOOL)
                .pattern(" N ")
                .pattern("NRN")
                .pattern(" N ")
                .unlockedBy("has_purple_wool", has(Items.PURPLE_WOOL))
                .save(exporter, getRecipePath("ott", "delicate_purple_wool"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.ORNAMENTED_PURPLE_CARPET.get(), 3)
                .define('W', ModBlocks.ORNAMENTED_PURPLE_WOOL.get())
                .pattern("WW")
                .unlockedBy("has_ornamented_purple_wool", has(ModBlocks.ORNAMENTED_PURPLE_WOOL.get()))
                .save(exporter, getRecipePath("ott", "ornamented_purple_carpet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.DELICATE_PURPLE_CARPET.get(), 3)
                .define('W', ModBlocks.DELICATE_PURPLE_WOOL.get())
                .pattern("WW")
                .unlockedBy("has_delicate_purple_wool", has(ModBlocks.DELICATE_PURPLE_WOOL.get()))
                .save(exporter, getRecipePath("ott", "delicate_purple_carpet"));

        // --- Glazed terracotta smelting (16 custom colors) ---
        ModBlocks.COLOR_SETS.forEach((color, set) ->
                SimpleCookingRecipeBuilder.smelting(Ingredient.of(set.terracotta().get()),
                                RecipeCategory.BUILDING_BLOCKS, set.glazedTerracotta().get(), 0.1F, 200)
                        .unlockedBy("has_terracotta", has(set.terracotta().get()))
                        .save(exporter, getRecipePath("ott", color + "_glazed_terracotta_from_smelting"))
        );

        // --- Dyed stone: 8 stone (ring) + dye → 8 dyed_stone (all 32 colors) ---
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));
            addRingDyeRecipe(exporter, Items.STONE, ModBlocks.PATTERN_BLOCKS.get("dyed_stone").get(name).get(), dye, name + "_dyed_stone");
        }
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item dye = ModItems.CUSTOM_DYES.get(name).get();
            addRingDyeRecipe(exporter, Items.STONE, ModBlocks.PATTERN_BLOCKS.get("dyed_stone").get(name).get(), dye, name + "_dyed_stone");
        }

        // --- Painted planks: 8 planks (ring, tag) + dye → 8 painted_planks (all 32 colors) ---
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PATTERN_BLOCKS.get("painted_planks").get(name).get(), 8)
                    .define('#', ItemTags.PLANKS)
                    .define('D', dye)
                    .pattern("###")
                    .pattern("#D#")
                    .pattern("###")
                    .unlockedBy("has_planks", has(ItemTags.PLANKS))
                    .save(exporter, getRecipePath("ott", name + "_painted_planks_from_dyeing"));
        }
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item dye = ModItems.CUSTOM_DYES.get(name).get();
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PATTERN_BLOCKS.get("painted_planks").get(name).get(), 8)
                    .define('#', ItemTags.PLANKS)
                    .define('D', dye)
                    .pattern("###")
                    .pattern("#D#")
                    .pattern("###")
                    .unlockedBy("has_planks", has(ItemTags.PLANKS))
                    .save(exporter, getRecipePath("ott", name + "_painted_planks_from_dyeing"));
        }


        // --- Stone bricks faucet: S/W/S (vertical center column) → 1 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.STONE_BRICKS_FAUCET.get())
                .define('S', Items.STONE_BRICKS)
                .define('W', Items.WATER_BUCKET)
                .pattern("S")
                .pattern("W")
                .pattern("S")
                .unlockedBy("has_stone_bricks", has(Items.STONE_BRICKS))
                .save(exporter, getRecipePath("ott", "stone_bricks_faucet"));

        // --- Stone bricks water jet: S/W/W (vertical center column) → 1 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.STONE_BRICKS_WATER_JET.get())
                .define('S', Items.STONE_BRICKS)
                .define('W', Items.WATER_BUCKET)
                .pattern("S")
                .pattern("W")
                .pattern("W")
                .unlockedBy("has_stone_bricks", has(Items.STONE_BRICKS))
                .save(exporter, getRecipePath("ott", "stone_bricks_water_jet"));

        // --- Water source trickle: W/W/W (vertical center column) → 1 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.WATER_SOURCE_TRICKLE.get())
                .define('W', Items.WATER_BUCKET)
                .pattern("W")
                .pattern("W")
                .pattern("W")
                .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                .save(exporter, getRecipePath("ott", "water_source_trickle"));

        // --- Weathering station: C_C/C_C/ICI (5 copper + 2 iron) → 1 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WEATHERING_STATION.get())
                .define('C', Items.COPPER_INGOT)
                .define('I', Items.IRON_INGOT)
                .pattern("C C")
                .pattern("C C")
                .pattern("ICI")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(exporter, getRecipePath("ott", "weathering_station"));

        // --- Stone lantern: SSS/GTG/SSS (6 stone + 2 glass panes + 1 torch) → 1 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.STONE_LANTERN.get())
                .define('S', Items.STONE)
                .define('G', Items.GLASS_PANE)
                .define('T', Items.TORCH)
                .pattern("SSS")
                .pattern("GTG")
                .pattern("SSS")
                .unlockedBy("has_stone", has(Items.STONE))
                .save(exporter, getRecipePath("ott", "stone_lantern"));

        // --- Iron fancy lantern: NIN/NTN/NIN (6 nuggets + 2 ingots + 1 torch) → 1 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.IRON_FANCY_LANTERN.get())
                .define('N', Items.IRON_NUGGET)
                .define('I', Items.IRON_INGOT)
                .define('T', Items.TORCH)
                .pattern("NIN")
                .pattern("NTN")
                .pattern("NIN")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(exporter, getRecipePath("ott", "iron_fancy_lantern"));

        // --- Bug net: diagonal (stick bottom-left, stick center, wool top-right) → 1 ---
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BUG_NET.get())
                .define('S', Items.STICK)
                .define('W', ItemTags.WOOL)
                .pattern("  W")
                .pattern(" S ")
                .pattern("S  ")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(exporter, getRecipePath("ott", "bug_net"));

        // --- Futons: 3 matching wool (top row) + 3 wooden slabs (bottom row) → 1 futon (all 32 colors) ---
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            Block woolBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(name + "_wool"));
            Block futon = ModBlocks.FUTONS.get(name).get();
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, futon)
                    .define('W', woolBlock)
                    .define('S', ItemTags.WOODEN_SLABS)
                    .pattern("WWW")
                    .pattern("SSS")
                    .unlockedBy("has_wool", has(woolBlock))
                    .save(exporter, getRecipePath("ott", name + "_futon"));
        }
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Block woolBlock = ModBlocks.COLOR_SETS.get(name).wool().get();
            Block futon = ModBlocks.FUTONS.get(name).get();
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, futon)
                    .define('W', woolBlock)
                    .define('S', ItemTags.WOODEN_SLABS)
                    .pattern("WWW")
                    .pattern("SSS")
                    .unlockedBy("has_wool", has(woolBlock))
                    .save(exporter, getRecipePath("ott", name + "_futon"));
        }
    }

    private void addRingDyeRecipe(RecipeOutput exporter, ItemLike source, ItemLike result, Item dye, String recipeName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 8)
                .define('#', source)
                .define('D', dye)
                .pattern("###")
                .pattern("#D#")
                .pattern("###")
                .unlockedBy("has_source", has(source))
                .save(exporter, getRecipePath("ott", recipeName + "_from_dyeing"));
    }

    private void addSingleDyeRecipe(RecipeOutput exporter, ItemLike source, ItemLike result, Item dye, String recipeName) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result)
                .requires(source)
                .requires(dye)
                .unlockedBy("has_source", has(source))
                .save(exporter, getRecipePath("ott", recipeName + "_from_dyeing_single"));
    }

    @SuppressWarnings("SameParameterValue")
    private void addRingShapeRecipe(RecipeOutput exporter, ItemLike ingredient, ItemLike result, int count, String recipeName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, count)
                .define('#', ingredient)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .unlockedBy("has_ingredient", has(ingredient))
                .save(exporter, getRecipePath("ott", recipeName));
    }

    private void mountsOfMayhemRecipes(RecipeOutput exporter) {
        {
            DeferredItem<AnimalArmorItem> i = ModItems.NETHERITE_HORSE_ARMOR;
            SmithingTransformRecipeBuilder.smithing(
                            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                            Ingredient.of(Items.DIAMOND_HORSE_ARMOR),
                            Ingredient.of(Items.NETHERITE_INGOT),
                            RecipeCategory.MISC,
                            i.get()
                    )
                    .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                    .save(exporter, getRecipePath("minecraft", "netherite_horse_armor_smithing"));
        }
    }

    private void addMiscRecipes(RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.CHEST, 4)
                .define('#', net.minecraft.tags.ItemTags.LOGS)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .unlockedBy("has_logs", has(net.minecraft.tags.ItemTags.LOGS))
                .save(exporter, getRecipePath("ott", "chest_from_logs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.HOPPER)
                .define('I', Items.IRON_INGOT)
                .define('L', net.minecraft.tags.ItemTags.LOGS)
                .pattern("ILI")
                .pattern("ILI")
                .pattern(" I ")
                .unlockedBy("has_logs", has(net.minecraft.tags.ItemTags.LOGS))
                .save(exporter, getRecipePath("ott", "hopper_from_logs"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.TORCH_ARROW.get(), 8)
                .requires(Items.ARROW)
                .requires(Items.TORCH)
                .unlockedBy("has_arrow", has(Items.ARROW))
                .unlockedBy("has_torch", has(Items.TORCH))
                .save(exporter, getRecipePath("ott", "torch_arrow"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, Items.SPECTRAL_ARROW, 2)
                .requires(Items.ARROW)
                .requires(ModItems.GLOW_GOOP.get(), 8)
                .unlockedBy("has_glow_goop", has(ModItems.GLOW_GOOP.get()))
                .save(exporter, getRecipePath("ott", "spectral_arrow_from_glow_goop"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.GLOW_BERRIES), RecipeCategory.MISC, ModItems.GLOW_GOOP.get(), 0.1F, 200)
                .unlockedBy("has_glow_berries", has(Items.GLOW_BERRIES))
                .save(exporter, getRecipePath("ott", "glow_goop_from_smelting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.GLOWSTONE), RecipeCategory.BUILDING_BLOCKS, ModBlocks.REFINED_GLOWSTONE.get(), 0.1F, 200)
                .unlockedBy("has_glowstone", has(Items.GLOWSTONE))
                .save(exporter, getRecipePath("ott", "refined_glowstone_from_smelting"));
    }

    private void addDyeingRecipes(RecipeOutput exporter) {
        // Vanilla Colors
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));

            registerDyeingGroup(exporter, dye, name,
                    getItem(name + "_banner"),
                    getItem(name + "_candle"),
                    getItem(name + "_stained_glass"),
                    getItem(name + "_stained_glass_pane"),
                    getItem(name + "_shulker_box"),
                    getItem(name + "_concrete"),
                    getItem(name + "_concrete_powder"),
                    getItem(name + "_terracotta"),
                    getItem(name + "_wool"),
                    getItem(name + "_bed"),
                    getItem(name + "_carpet"),
                    false
            );
        }

        // Custom Colors
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item dye = ModItems.CUSTOM_DYES.get(name).get();
            ModBlocks.ColorSetBlocks blocks = ModBlocks.COLOR_SETS.get(name);

            registerDyeingGroup(exporter, dye, name,
                    blocks.banner().get().asItem(),
                    blocks.candle().get().asItem(),
                    blocks.stainedGlass().get().asItem(),
                    blocks.stainedGlassPane().get().asItem(),
                    blocks.shulkerBox().get().asItem(),
                    blocks.concrete().get().asItem(),
                    blocks.concretePowder().get().asItem(),
                    blocks.terracotta().get().asItem(),
                    blocks.wool().get().asItem(),
                    blocks.bed().get().asItem(),
                    blocks.carpet().get().asItem(),
                    true
            );
        }
    }

    private static final Set<String> DEDICATED_PATTERN_RECIPES =
            Set.of("flat_roof_tiles", "layered_roof_tiles", "plastered_stone",
                   "chiseled_plastered_stone", "gilded_plastered_stone",
                   "delicate_plastered_stone", "banded_plastered_stone",
                   "dyed_stone", "painted_planks");

    private void addPatternBlockRecipes(RecipeOutput exporter) {
        for (Map.Entry<String, Map<String, DeferredBlock<Block>>> patternEntry : ModBlocks.PATTERN_BLOCKS.entrySet()) {
            String pattern = patternEntry.getKey();
            if (DEDICATED_PATTERN_RECIPES.contains(pattern)) continue;
            Map<String, DeferredBlock<Block>> colorMap = patternEntry.getValue();

            for (DyeColor color : DyeColor.values()) {
                String name = color.getName();
                DeferredBlock<Block> block = colorMap.get(name);
                if (block == null) continue;
                Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));
                addDyedPatternRecipe(exporter, block.get(), dye, name + "_" + pattern);
            }

            for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
                String name = colorSet.name();
                DeferredBlock<Block> block = colorMap.get(name);
                if (block == null) continue;
                Item dye = ModItems.CUSTOM_DYES.get(name).get();
                addDyedPatternRecipe(exporter, block.get(), dye, name + "_" + pattern);
            }
        }
    }

    private void addDyedPatternRecipe(RecipeOutput exporter, Block result, Item dye, String recipeName) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result, 8)
                .requires(Items.COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(Items.COBBLESTONE)
                .requires(dye)
                .unlockedBy("has_dye", has(dye))
                .save(exporter, getRecipePath("ott", recipeName + "_from_dyeing"));
    }

    private void addElevatorRecipes(RecipeOutput exporter) {
        // Craft: 8 matching wool + 1 ender pearl (3x3, pearl in center) → 8 elevators
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            com.otterly76.ott.block.custom.ElevatorBlock elevator = ModBlocks.ELEVATORS.get(name).get();
            Block woolBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(name + "_wool"));
            ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, elevator, 8)
                    .define('W', woolBlock)
                    .define('E', Items.ENDER_PEARL)
                    .pattern("WWW")
                    .pattern("WEW")
                    .pattern("WWW")
                    .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                    .save(exporter, getRecipePath("ott", name + "_elevator"));
        }

        // Vanilla-color redye: any elevator + vanilla dye → vanilla-colored elevator
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();
            Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name + "_dye"));
            Block result = ModBlocks.ELEVATORS.get(name).get();
            ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, result)
                    .requires(net.minecraft.tags.ItemTags.create(ResourceLocation.fromNamespaceAndPath("ott", "elevators")))
                    .requires(dye)
                    .unlockedBy("has_elevator", has(net.minecraft.tags.ItemTags.create(ResourceLocation.fromNamespaceAndPath("ott", "elevators"))))
                    .save(exporter, getRecipePath("ott", name + "_elevator_from_dyeing"));
        }

        // Custom-color: 8 matching custom wool + 1 ender eye → 8 elevators
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Block woolBlock = ModBlocks.COLOR_SETS.get(name).wool().get();
            Block result = ModBlocks.ELEVATORS.get(name).get();
            ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, result, 8)
                    .define('W', woolBlock)
                    .define('E', Items.ENDER_PEARL)
                    .pattern("WWW")
                    .pattern("WEW")
                    .pattern("WWW")
                    .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                    .save(exporter, getRecipePath("ott", name + "_elevator"));
        }

        // Custom-color redye: any elevator + custom dye → custom-colored elevator
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String name = colorSet.name();
            Item dye = ModItems.CUSTOM_DYES.get(name).get();
            Block result = ModBlocks.ELEVATORS.get(name).get();
            ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, result)
                    .requires(net.minecraft.tags.ItemTags.create(ResourceLocation.fromNamespaceAndPath("ott", "elevators")))
                    .requires(dye)
                    .unlockedBy("has_elevator", has(net.minecraft.tags.ItemTags.create(ResourceLocation.fromNamespaceAndPath("ott", "elevators"))))
                    .save(exporter, getRecipePath("ott", name + "_elevator_from_dyeing"));
        }
    }

    private Item getItem(String name) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(name));
    }

    private void registerDyeingGroup(RecipeOutput exporter, Item dye, String colorName,
                                     Item banner, Item candle, Item glass, Item pane, Item shulker,
                                     Item concrete, Item powder, Item terracotta, Item wool, Item bed, Item carpet,
                                     boolean isCustom) {
        // 1:1 Shapeless
        addDyeingRecipe(exporter, banner, ModTags.ItemTags.DYEABLE_BANNERS, dye, colorName + "_banner", "has_any_banner");
        addDyeingRecipe(exporter, candle, ModTags.ItemTags.DYEABLE_CANDLES, dye, colorName + "_candle", "has_any_candle");
        addDyeingRecipe(exporter, glass, ModTags.ItemTags.DYEABLE_GLASS_BLOCKS, dye, colorName + "_glass", "has_any_glass");
        addDyeingRecipe(exporter, pane, ModTags.ItemTags.DYEABLE_GLASS_PANES, dye, colorName + "_pane", "has_any_pane");
        addDyeingRecipe(exporter, shulker, ModTags.ItemTags.DYEABLE_SHULKER_BOXES, dye, colorName + "_shulker_box", "has_any_shulker");
        addDyeingRecipe(exporter, concrete, ModTags.ItemTags.DYEABLE_CONCRETE, dye, colorName + "_concrete", "has_any_concrete");
        addDyeingRecipe(exporter, powder, ModTags.ItemTags.DYEABLE_CONCRETE_POWDER, dye, colorName + "_concrete_powder", "has_any_powder");
        addDyeingRecipe(exporter, terracotta, ModTags.ItemTags.DYEABLE_TERRACOTTA, dye, colorName + "_terracotta", "has_any_terracotta");

        addDyeingRecipe(exporter, wool, ItemTags.WOOL, dye, colorName + "_wool", "has_any_wool");
        addDyeingRecipe(exporter, bed, ItemTags.BEDS, dye, colorName + "_bed", "has_any_bed");
        addDyeingRecipe(exporter, carpet, ItemTags.WOOL_CARPETS, dye, colorName + "_carpet", "has_any_carpet");

        // 8:1 Shaped
        addShapedDyeingRecipe8(exporter, wool, ItemTags.WOOL, dye, colorName + "_wool", "has_any_wool");
        addShapedDyeingRecipe8(exporter, carpet, ItemTags.WOOL_CARPETS, dye, colorName + "_carpet", "has_any_carpet");
        addShapedDyeingRecipe8(exporter, glass, ModTags.ItemTags.DYEABLE_GLASS_BLOCKS, dye, colorName + "_glass", "has_any_glass");
        addShapedDyeingRecipe8(exporter, pane, ModTags.ItemTags.DYEABLE_GLASS_PANES, dye, colorName + "_pane", "has_any_pane");
        addShapedDyeingRecipe8(exporter, terracotta, ModTags.ItemTags.DYEABLE_TERRACOTTA, dye, colorName + "_terracotta", "has_any_terracotta");
        addShapedDyeingRecipe8(exporter, candle, ModTags.ItemTags.DYEABLE_CANDLES, dye, colorName + "_candle", "has_any_candle");
        addShapedDyeingRecipe8(exporter, concrete, ModTags.ItemTags.DYEABLE_CONCRETE, dye, colorName + "_concrete", "has_any_concrete");
        addShapedDyeingRecipe8(exporter, powder, ModTags.ItemTags.DYEABLE_CONCRETE_POWDER, dye, colorName + "_concrete_powder", "has_any_powder");

        if (isCustom) {
            addConcretePowderCrafting(exporter, powder, dye, colorName);
        }
    }

    private void addConcretePowderCrafting(RecipeOutput exporter, Item result, Item dye, String colorName) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result, 8)
                .requires(dye)
                .requires(ItemTags.SAND)
                .requires(ItemTags.SAND)
                .requires(ItemTags.SAND)
                .requires(ItemTags.SAND)
                .requires(Items.GRAVEL)
                .requires(Items.GRAVEL)
                .requires(Items.GRAVEL)
                .requires(Items.GRAVEL)
                .unlockedBy("has_dye", has(dye))
                .save(exporter, getRecipePath("ott", colorName + "_concrete_powder_crafting"));
    }

    private void addDyeingRecipe(RecipeOutput exporter, Item result, TagKey<Item> ingredientTag, Item dye, String recipeName, String criterionName) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result)
                .requires(ingredientTag)
                .requires(dye)
                .unlockedBy(criterionName, has(ingredientTag))
                .save(exporter, getRecipePath("ott", recipeName + "_from_dyeing"));
    }

    private void addShapedDyeingRecipe8(RecipeOutput exporter, Item result, TagKey<Item> ingredientTag, Item dye, String recipeName, String criterionName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 8)
                .define('#', ingredientTag)
                .define('D', dye)
                .pattern("###")
                .pattern("#D#")
                .pattern("###")
                .unlockedBy(criterionName, has(ingredientTag))
                .save(exporter, getRecipePath("ott", recipeName + "_from_dyeing_8"));
    }

    private void shelfRecipes(RecipeOutput noAdv) {
        String[] shelfWoods = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped", "pale_oak"};
        for (int i = 0; i < shelfWoods.length; i++) {
            String wood = shelfWoods[i];
            Block shelf = ModBlocks.SHELVES.get(i).get();
            Item log = switch (wood) {
                case "pale_oak" -> ModBlocks.STRIPPED_PALE_OAK_LOG.get().asItem();
                case "crimson", "warped" ->
                        BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace("stripped_" + wood + "_stem"));
                case "bamboo" -> Items.STRIPPED_BAMBOO_BLOCK;
                default ->
                        BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace("stripped_" + wood + "_log"));
            };

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, shelf, 6)
                    .define('#', log)
                    .pattern("###")
                    .pattern("   ")
                    .pattern("###")
                    .unlockedBy("impossible", impossible())
                    .save(noAdv, getRecipePath("minecraft", wood + "_shelf"));
        }
        // OTT wood set shelves
        ModBlocks.WOOD_SETS.forEach((wood, set) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.shelf().get(), 6)
                        .define('#', set.strippedLog().get())
                        .pattern("###")
                        .pattern("   ")
                        .pattern("###")
                        .unlockedBy("impossible", impossible())
                        .save(noAdv, getRecipePath("ott", wood + "_shelf")));
    }

    private void copperRecipes(RecipeOutput noAdv) {
        // Nuggets <-> Ingot
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COPPER_NUGGET.get(), 9)
                .requires(Items.COPPER_INGOT)
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "copper_nugget"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT)
                .define('#', ModItems.COPPER_NUGGET.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "copper_ingot_from_nuggets"));

        // Smelting / Blasting for Nuggets
        Item[] smeltables = {
                ModItems.COPPER_PICKAXE.get(), ModItems.COPPER_SHOVEL.get(), ModItems.COPPER_AXE.get(),
                ModItems.COPPER_HOE.get(), ModItems.COPPER_SWORD.get(), ModItems.COPPER_HELMET.get(),
                ModItems.COPPER_CHESTPLATE.get(), ModItems.COPPER_LEGGINGS.get(), ModItems.COPPER_BOOTS.get(),
                ModItems.COPPER_HORSE_ARMOR.get()
        };
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(smeltables), RecipeCategory.MISC, ModItems.COPPER_NUGGET.get(), 0.1F, 200)
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "copper_nugget_from_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(smeltables), RecipeCategory.MISC, ModItems.COPPER_NUGGET.get(), 0.1F, 100)
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "copper_nugget_from_blasting"));

        // Copper Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_BARS.get("").get(), 16)
                .define('#', Items.COPPER_INGOT).pattern("###").pattern("###")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_bars"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.COPPER_BUTTONS.get("").get())
                .requires(Items.COPPER_INGOT)
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_button"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_CHAINS.get("").get())
                .define('I', Items.COPPER_INGOT).define('N', ModItems.COPPER_NUGGET.get()).pattern("N").pattern("I").pattern("N")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_chain"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_CHEST.get())
                .define('#', Items.COPPER_INGOT).pattern("###").pattern("# #").pattern("###")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_chest"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_DOORS.get("").get(), 3)
                .define('#', Items.COPPER_INGOT).pattern("##").pattern("##").pattern("##")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_door"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_TRAPDOORS.get("").get(), 2)
                .define('#', Items.COPPER_INGOT).pattern("###").pattern("###")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_trapdoor"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_PRESSURE_PLATES.get("").get())
                .define('#', Items.COPPER_INGOT).pattern("##")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_pressure_plate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_LANTERNS.get("").get())
                .define('#', ModItems.COPPER_NUGGET.get()).define('X', Items.TORCH).pattern("###").pattern("#X#").pattern("###")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_lantern"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_TORCH.get(), 4)
                .define('#', Items.COAL).define('S', Items.COPPER_INGOT).pattern("#").pattern("S")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_torch"));

        // Waxing recipes and button conversion
        String[] states = {"", "exposed_", "weathered_", "oxidized_"};
        for (String state : states) {
            registerWaxing(noAdv, ModBlocks.COPPER_BARS.get(state).get(), ModBlocks.COPPER_BARS.get("waxed_" + state).get());
            registerWaxing(noAdv, ModBlocks.COPPER_BUTTONS.get(state).get(), ModBlocks.COPPER_BUTTONS.get("waxed_" + state).get());
            registerWaxing(noAdv, ModBlocks.COPPER_CHAINS.get(state).get(), ModBlocks.COPPER_CHAINS.get("waxed_" + state).get());
            registerWaxing(noAdv, ModBlocks.COPPER_DOORS.get(state).get(), ModBlocks.COPPER_DOORS.get("waxed_" + state).get());
            registerWaxing(noAdv, ModBlocks.COPPER_TRAPDOORS.get(state).get(), ModBlocks.COPPER_TRAPDOORS.get("waxed_" + state).get());
            registerWaxing(noAdv, ModBlocks.COPPER_PRESSURE_PLATES.get(state).get(), ModBlocks.COPPER_PRESSURE_PLATES.get("waxed_" + state).get());
            registerWaxing(noAdv, ModBlocks.COPPER_LANTERNS.get(state).get(), ModBlocks.COPPER_LANTERNS.get("waxed_" + state).get());
            registerWaxing(noAdv, ModBlocks.COPPER_GOLEM_STATUES.get(state).get(), ModBlocks.COPPER_GOLEM_STATUES.get("waxed_" + state).get());
            
            if (ModBlocks.LIGHTNING_RODS.containsKey(state)) {
                registerWaxing(noAdv, ModBlocks.LIGHTNING_RODS.get(state).get(), ModBlocks.LIGHTNING_RODS.get("waxed_" + state).get());
            } else if (state.isEmpty()) {
                registerWaxing(noAdv, Items.LIGHTNING_ROD, ModBlocks.LIGHTNING_RODS.get("waxed_").get());
            }

            // Button from cut copper
            Item cutCopper = state.isEmpty() ? Items.CUT_COPPER : BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(state + "cut_copper"));
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_BUTTONS.get(state).get())
                    .define('#', cutCopper)
                    .pattern("#")
                    .unlockedBy("impossible", impossible())
                    .save(noAdv, getRecipePath("minecraft", state + "copper_button_from_" + state + "cut_copper"));

            // Waxed button from waxed cut copper
            Item waxedCutCopper = state.isEmpty() ? Items.WAXED_CUT_COPPER : BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace("waxed_" + state + "cut_copper"));
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_BUTTONS.get("waxed_" + state).get())
                    .define('#', waxedCutCopper)
                    .pattern("#")
                    .unlockedBy("impossible", impossible())
                    .save(noAdv, getRecipePath("minecraft", "waxed_" + state + "copper_button_from_waxed_" + state + "cut_copper"));

            // Pressure plate from cut copper
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_PRESSURE_PLATES.get(state).get())
                    .define('#', cutCopper)
                    .pattern("##")
                    .unlockedBy("impossible", impossible())
                    .save(noAdv, getRecipePath("minecraft", state + "copper_pressure_plate_from_" + state + "cut_copper"));

            // Waxed pressure plate from waxed cut copper
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_PRESSURE_PLATES.get("waxed_" + state).get())
                    .define('#', waxedCutCopper)
                    .pattern("##")
                    .unlockedBy("impossible", impossible())
                    .save(noAdv, getRecipePath("minecraft", "waxed_" + state + "copper_pressure_plate_from_waxed_" + state + "cut_copper"));
        }
        
        registerWaxing(noAdv, ModBlocks.COPPER_CHEST.get(), ModBlocks.WAXED_COPPER_CHEST.get());
        registerWaxing(noAdv, ModBlocks.EXPOSED_COPPER_CHEST.get(), ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get());
        registerWaxing(noAdv, ModBlocks.WEATHERED_COPPER_CHEST.get(), ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get());
        registerWaxing(noAdv, ModBlocks.OXIDIZED_COPPER_CHEST.get(), ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get());
    }

    private void registerWaxing(RecipeOutput noAdv, ItemLike unaffected, ItemLike waxed) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, waxed)
                .requires(unaffected)
                .requires(Items.HONEYCOMB)
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", BuiltInRegistries.ITEM.getKey(waxed.asItem()).getPath() + "_from_honeycomb"));
    }

    private void copperToolArmorRecipes(RecipeOutput noAdv) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_SWORD.get())
                .define('#', Items.COPPER_INGOT).define('S', Items.STICK).pattern("#").pattern("#").pattern("S")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_sword"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_SHOVEL.get())
                .define('#', Items.COPPER_INGOT).define('S', Items.STICK).pattern("#").pattern("S").pattern("S")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_shovel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_PICKAXE.get())
                .define('#', Items.COPPER_INGOT).define('S', Items.STICK).pattern("###").pattern(" S ").pattern(" S ")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_pickaxe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_AXE.get())
                .define('#', Items.COPPER_INGOT).define('S', Items.STICK).pattern("##").pattern("#S").pattern(" S")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_axe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_HOE.get())
                .define('#', Items.COPPER_INGOT).define('S', Items.STICK).pattern("##").pattern(" S").pattern(" S")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_hoe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_HELMET.get())
                .define('#', Items.COPPER_INGOT).pattern("###").pattern("# #")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_helmet"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_CHESTPLATE.get())
                .define('#', Items.COPPER_INGOT).pattern("# #").pattern("###").pattern("###")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_chestplate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_LEGGINGS.get())
                .define('#', Items.COPPER_INGOT).pattern("###").pattern("# #").pattern("# #")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_leggings"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_BOOTS.get())
                .define('#', Items.COPPER_INGOT).pattern("# #").pattern("# #")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_boots"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COPPER_HORSE_ARMOR.get())
                .define('#', Items.COPPER_INGOT).define('S', Items.LEATHER)
                .pattern("# #").pattern("#S#").pattern("# #")
                .unlockedBy("impossible", impossible()).save(noAdv, getRecipePath("minecraft", "copper_horse_armor"));
    }

    private void woodRecipes(RecipeOutput noAdv) {
        // --- Backported Pale Oak recipes (minecraft namespace) ---
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_PLANKS.get(), 4)
                .requires(ModTags.ItemTags.PALE_OAK_LOGS)
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_planks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_STAIRS.get(), 4)
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_SLAB.get(), 6)
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PALE_OAK_FENCE.get(), 3)
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .define('S', Items.STICK)
                .pattern("#S#")
                .pattern("#S#")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_fence"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_FENCE_GATE.get())
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .define('S', Items.STICK)
                .pattern("S#S")
                .pattern("S#S")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_fence_gate"));

        // sign + hanging sign (you said you want both)
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.PALE_OAK_SIGN.get(), 3)
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .define('S', Items.STICK)
                .pattern("###")
                .pattern("###")
                .pattern(" S ")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.PALE_OAK_HANGING_SIGN.get(), 6)
                .define('#', ModBlocks.STRIPPED_PALE_OAK_LOG.get())
                .define('C', Items.CHAIN)
                .pattern("C C")
                .pattern("###")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_hanging_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_DOOR.get(), 3)
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_door"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_TRAPDOOR.get(), 2)
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_trapdoor"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModItems.PALE_OAK_BOAT.get())
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .pattern("# #")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_boat"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.PALE_OAK_CHEST_BOAT.get())
                .requires(ModItems.PALE_OAK_BOAT.get())
                .requires(Items.CHEST)
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_chest_boat"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_PALE_OAK_WOOD.get(), 3)
                .define('#', ModBlocks.STRIPPED_PALE_OAK_LOG.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "stripped_pale_oak_wood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_WOOD.get(), 3)
                .define('#', ModBlocks.PALE_OAK_LOG.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_wood"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_BUTTON.get())
                .requires(ModBlocks.PALE_OAK_PLANKS.get())
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_button"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PALE_OAK_PRESSURE_PLATE.get())
                .define('#', ModBlocks.PALE_OAK_PLANKS.get())
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "pale_oak_pressure_plate"));

        // --- ott wood sets (ott namespace) ---
        ModBlocks.WOOD_SETS.forEach((setName, set) -> registerOttWoodSetRecipes(noAdv, setName, set));
    }

    private void registerOttWoodSetRecipes(RecipeOutput noAdv, String setName, ModBlocks.WoodSetBlocks set) {
        // Use tag-based “any log variant” per set: ott:<setName>_logs
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, set.planks().get(), 4)
                .requires(ModTags.ItemTags.woodSetLogs(setName))
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.planks().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.stairs().get(), 4)
                .define('#', set.planks().get())
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.stairs().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.slab().get(), 6)
                .define('#', set.planks().get())
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.slab().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, set.fence().get(), 3)
                .define('#', set.planks().get())
                .define('S', Items.STICK)
                .pattern("#S#")
                .pattern("#S#")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.fence().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, set.fenceGate().get())
                .define('#', set.planks().get())
                .define('S', Items.STICK)
                .pattern("S#S")
                .pattern("S#S")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.fenceGate().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, set.door().get(), 3)
                .define('#', set.planks().get())
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.door().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, set.trapdoor().get(), 2)
                .define('#', set.planks().get())
                .pattern("###")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.trapdoor().getId().getPath()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, set.button().get())
                .requires(set.planks().get())
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.button().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, set.pressurePlate().get())
                .define('#', set.planks().get())
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.pressurePlate().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.wood().get(), 3)
                .define('#', set.log().get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.wood().getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.strippedWood().get(), 3)
                .define('#', set.strippedLog().get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", set.strippedWood().getId().getPath()));

        // Vanilla sign recipe: 6 planks + 1 stick -> 3 signs
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.WOOD_SET_SIGNS.get(setName).get(), 3)
                .define('#', set.planks().get())
                .define('S', Items.STICK)
                .pattern("###")
                .pattern("###")
                .pattern(" S ")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", setName + "_sign"));

        // Vanilla hanging sign recipe: chains + stripped logs -> 6 hanging signs
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.WOOD_SET_HANGING_SIGNS.get(setName).get(), 6)
                .define('#', set.strippedLog().get())
                .define('C', Items.CHAIN)
                .pattern("C C")
                .pattern("###")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", setName + "_hanging_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModItems.WOOD_SET_BOATS.get(setName).get())
                .define('#', set.planks().get())
                .pattern("# #")
                .pattern("###")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", setName + "_boat"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModItems.WOOD_SET_CHEST_BOATS.get(setName).get())
                .requires(ModItems.WOOD_SET_BOATS.get(setName).get())
                .requires(Items.CHEST)
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", setName + "_chest_boat"));
    }

    private void createGradientRecipe(RecipeOutput noAdv, IGradientBlock gradientBlock) {
        Block block = (Block) gradientBlock;
        Block ingredient1 = gradientBlock.getBlockFromColor(gradientBlock.getFirstColor());
        Block ingredient2 = gradientBlock.getBlockFromColor(gradientBlock.getSecondColor());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, block, 2)
                .requires(ingredient1)
                .requires(ingredient2)
                .group("ott_gradient_blocks")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("ott", gradientBlock.getRegistryID().getPath()));
    }

    private void ottCrittersRecipes(RecipeOutput noAdv) {
        // Smelting
        this.cooking(noAdv, List.of(ModItems.RAW_GOLDEN_SUNFISH_MEAT.get()), ModItems.COOKED_GOLDEN_SUNFISH_MEAT.get(), "cooked_golden_sunfish_meat");
        this.cooking(noAdv, List.of(ModItems.RAW_KRILL.get()), ModItems.FRIED_KRILL.get(), "fried_krill");
        this.cooking(noAdv, List.of(ModItems.RAW_SUNFISH_MEAT.get()), ModItems.COOKED_SUNFISH_MEAT.get(), "cooked_sunfish_meat");

        // Crafting
        // Salt
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SALT_BLOCK.get())
                .define('#', ModItems.SALT.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_salt", has(ModItems.SALT.get()))
                .save(noAdv, getRecipePath("ott", "salt_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_SALT_BLOCK.get(), 4)
                .define('#', ModBlocks.SALT_BLOCK.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_salt_block", has(ModBlocks.SALT_BLOCK.get()))
                .save(noAdv, getRecipePath("ott", "polished_salt_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SALT_LAMP.get())
                .define('S', ModBlocks.POLISHED_SALT_BLOCK.get())
                .define('G', Items.GLOWSTONE_DUST)
                .pattern(" S ")
                .pattern("SGS")
                .pattern(" S ")
                .unlockedBy("has_polished_salt_block", has(ModBlocks.POLISHED_SALT_BLOCK.get()))
                .save(noAdv, getRecipePath("ott", "salt_lamp"));

        // Catfish
        cooking(noAdv, List.of(ModItems.CATFISH.get()), ModItems.COOKED_CATFISH.get(), "cooked_catfish");
        // Bass
        cooking(noAdv, List.of(ModItems.BASS.get()), ModItems.COOKED_BASS.get(), "cooked_bass");
        // Snail
        cooking(noAdv, List.of(ModItems.RAW_SNAIL.get()), ModItems.COOKED_SNAIL.get(), "cooked_snail");
        // Shrimp
        cooking(noAdv, List.of(ModItems.RAW_SHRIMP.get()), ModItems.STEAMED_SHRIMP.get(), "steamed_shrimp");
        // Wild bird meat
        cooking(noAdv, List.of(ModItems.RAW_WILD_BIRD_MEAT.get()), ModItems.COOKED_WILD_BIRD_MEAT.get(), "cooked_wild_bird_meat");
        // Wild game meat
        cooking(noAdv, List.of(ModItems.RAW_WILD_GAME_MEAT.get()), ModItems.COOKED_WILD_GAME_MEAT.get(), "cooked_wild_game_meat");
        // Crab meat
        cooking(noAdv, List.of(ModItems.RAW_CRAB_MEAT.get()), ModItems.STEAMED_CRAB_MEAT.get(), "steamed_crab_meat");
        // Bonnethead shark
        cooking(noAdv, List.of(ModItems.RAW_BONNETHEAD.get()), ModItems.COOKED_BONNETHEAD.get(), "cooked_bonnethead");
        // Cichlid
        cooking(noAdv, List.of(ModItems.RAW_CICHLID.get()), ModItems.COOKED_CICHLID.get(), "cooked_cichlid");
        // Goblin shark
        cooking(noAdv, List.of(ModItems.RAW_GOBLIN_SHARK.get()), ModItems.COOKED_GOBLIN_SHARK.get(), "cooked_goblin_shark");
        // Guitarfish
        cooking(noAdv, List.of(ModItems.RAW_GUITARFISH.get()), ModItems.COOKED_GUITARFISH.get(), "cooked_guitarfish");
        // Lizard tail
        cooking(noAdv, List.of(ModItems.LIZARD_TAIL.get()), ModItems.COOKED_LIZARD_TAIL.get(), "cooked_lizard_tail");
        // Egg
        cooking(noAdv, List.of(Items.EGG), ModItems.COOKED_EGG.get(), "cooked_egg");
    }

    private void cooking(RecipeOutput exporter, List<ItemLike> ingredients, ItemLike result, String name) {
        int cookingTime = 200;
        float experience = 0.35F;
        RecipeCategory category = RecipeCategory.FOOD;
        oreSmelting(exporter, ingredients, category, result, experience, cookingTime, name);
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredients.toArray(new ItemLike[0])), category, result, experience, cookingTime / 2)
                .unlockedBy("has_" + name, has(ingredients.getFirst()))
                .save(exporter, getRecipePath("ott", name + "_from_smoking"));
    }

    private void registerSlabToBlock(RecipeOutput exporter, Item slab, Item block, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, block)
                .define('#', slab)
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_" + name, has(slab))
                .save(exporter, getRecipePath("ott", name + "_from_slabs"));
    }

    private void addSlabToBlockRecipes(RecipeOutput exporter) {
        // Vanilla Wood
        registerSlabToBlock(exporter, Items.OAK_SLAB, Items.OAK_PLANKS, "oak_planks");
        registerSlabToBlock(exporter, Items.SPRUCE_SLAB, Items.SPRUCE_PLANKS, "spruce_planks");
        registerSlabToBlock(exporter, Items.BIRCH_SLAB, Items.BIRCH_PLANKS, "birch_planks");
        registerSlabToBlock(exporter, Items.JUNGLE_SLAB, Items.JUNGLE_PLANKS, "jungle_planks");
        registerSlabToBlock(exporter, Items.ACACIA_SLAB, Items.ACACIA_PLANKS, "acacia_planks");
        registerSlabToBlock(exporter, Items.DARK_OAK_SLAB, Items.DARK_OAK_PLANKS, "dark_oak_planks");
        registerSlabToBlock(exporter, Items.MANGROVE_SLAB, Items.MANGROVE_PLANKS, "mangrove_planks");
        registerSlabToBlock(exporter, Items.CHERRY_SLAB, Items.CHERRY_PLANKS, "cherry_planks");
        registerSlabToBlock(exporter, Items.BAMBOO_SLAB, Items.BAMBOO_PLANKS, "bamboo_planks");
        registerSlabToBlock(exporter, Items.BAMBOO_MOSAIC_SLAB, Items.BAMBOO_MOSAIC, "bamboo_mosaic");
        registerSlabToBlock(exporter, Items.CRIMSON_SLAB, Items.CRIMSON_PLANKS, "crimson_planks");
        registerSlabToBlock(exporter, Items.WARPED_SLAB, Items.WARPED_PLANKS, "warped_planks");

        // Stones
        registerSlabToBlock(exporter, Items.STONE_SLAB, Items.STONE, "stone");
        registerSlabToBlock(exporter, Items.COBBLESTONE_SLAB, Items.COBBLESTONE, "cobblestone");
        registerSlabToBlock(exporter, Items.MOSSY_COBBLESTONE_SLAB, Items.MOSSY_COBBLESTONE, "mossy_cobblestone");
        registerSlabToBlock(exporter, Items.SMOOTH_STONE_SLAB, Items.SMOOTH_STONE, "smooth_stone");
        registerSlabToBlock(exporter, Items.STONE_BRICK_SLAB, Items.STONE_BRICKS, "stone_bricks");
        registerSlabToBlock(exporter, Items.MOSSY_STONE_BRICK_SLAB, Items.MOSSY_STONE_BRICKS, "mossy_stone_bricks");
        registerSlabToBlock(exporter, Items.GRANITE_SLAB, Items.GRANITE, "granite");
        registerSlabToBlock(exporter, Items.POLISHED_GRANITE_SLAB, Items.POLISHED_GRANITE, "polished_granite");
        registerSlabToBlock(exporter, Items.DIORITE_SLAB, Items.DIORITE, "diorite");
        registerSlabToBlock(exporter, Items.POLISHED_DIORITE_SLAB, Items.POLISHED_DIORITE, "polished_diorite");
        registerSlabToBlock(exporter, Items.ANDESITE_SLAB, Items.ANDESITE, "andesite");
        registerSlabToBlock(exporter, Items.POLISHED_ANDESITE_SLAB, Items.POLISHED_ANDESITE, "polished_andesite");

        // Deepslate
        registerSlabToBlock(exporter, Items.COBBLED_DEEPSLATE_SLAB, Items.COBBLED_DEEPSLATE, "cobbled_deepslate");
        registerSlabToBlock(exporter, Items.POLISHED_DEEPSLATE_SLAB, Items.POLISHED_DEEPSLATE, "polished_deepslate");
        registerSlabToBlock(exporter, Items.DEEPSLATE_BRICK_SLAB, Items.DEEPSLATE_BRICKS, "deepslate_bricks");
        registerSlabToBlock(exporter, Items.DEEPSLATE_TILE_SLAB, Items.DEEPSLATE_TILES, "deepslate_tiles");

        // Blackstone
        registerSlabToBlock(exporter, Items.BLACKSTONE_SLAB, Items.BLACKSTONE, "blackstone");
        registerSlabToBlock(exporter, Items.POLISHED_BLACKSTONE_SLAB, Items.POLISHED_BLACKSTONE, "polished_blackstone");
        registerSlabToBlock(exporter, Items.POLISHED_BLACKSTONE_BRICK_SLAB, Items.POLISHED_BLACKSTONE_BRICKS, "polished_blackstone_bricks");

        // Sandstone
        registerSlabToBlock(exporter, Items.SANDSTONE_SLAB, Items.SANDSTONE, "sandstone");
        registerSlabToBlock(exporter, Items.SMOOTH_SANDSTONE_SLAB, Items.SMOOTH_SANDSTONE, "smooth_sandstone");
        registerSlabToBlock(exporter, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace("cut_sandstone_slab")), Items.CUT_SANDSTONE, "cut_sandstone");
        registerSlabToBlock(exporter, Items.RED_SANDSTONE_SLAB, Items.RED_SANDSTONE, "red_sandstone");
        registerSlabToBlock(exporter, Items.SMOOTH_RED_SANDSTONE_SLAB, Items.SMOOTH_RED_SANDSTONE, "smooth_red_sandstone");
        registerSlabToBlock(exporter, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace("cut_red_sandstone_slab")), Items.CUT_RED_SANDSTONE, "cut_red_sandstone");

        // Tuff
        registerSlabToBlock(exporter, Items.TUFF_SLAB, Items.TUFF, "tuff");
        registerSlabToBlock(exporter, Items.POLISHED_TUFF_SLAB, Items.POLISHED_TUFF, "polished_tuff");
        registerSlabToBlock(exporter, Items.TUFF_BRICK_SLAB, Items.TUFF_BRICKS, "tuff_bricks");

        // Prismarine
        registerSlabToBlock(exporter, Items.PRISMARINE_SLAB, Items.PRISMARINE, "prismarine");
        registerSlabToBlock(exporter, Items.PRISMARINE_BRICK_SLAB, Items.PRISMARINE_BRICKS, "prismarine_bricks");
        registerSlabToBlock(exporter, Items.DARK_PRISMARINE_SLAB, Items.DARK_PRISMARINE, "dark_prismarine");

        // Misc
        registerSlabToBlock(exporter, Items.BRICK_SLAB, Items.BRICKS, "bricks");
        registerSlabToBlock(exporter, Items.MUD_BRICK_SLAB, Items.MUD_BRICKS, "mud_bricks");
        registerSlabToBlock(exporter, Items.NETHER_BRICK_SLAB, Items.NETHER_BRICKS, "nether_bricks");
        registerSlabToBlock(exporter, Items.RED_NETHER_BRICK_SLAB, Items.RED_NETHER_BRICKS, "red_nether_bricks");
        registerSlabToBlock(exporter, Items.QUARTZ_SLAB, Items.QUARTZ_BLOCK, "quartz_block");
        registerSlabToBlock(exporter, Items.SMOOTH_QUARTZ_SLAB, Items.SMOOTH_QUARTZ, "smooth_quartz");
        registerSlabToBlock(exporter, Items.PURPUR_SLAB, Items.PURPUR_BLOCK, "purpur_block");
        registerSlabToBlock(exporter, Items.END_STONE_BRICK_SLAB, Items.END_STONE_BRICKS, "end_stone_bricks");

        // Copper
        registerSlabToBlock(exporter, Items.CUT_COPPER_SLAB, Items.CUT_COPPER, "cut_copper");
        registerSlabToBlock(exporter, Items.EXPOSED_CUT_COPPER_SLAB, Items.EXPOSED_CUT_COPPER, "exposed_cut_copper");
        registerSlabToBlock(exporter, Items.WEATHERED_CUT_COPPER_SLAB, Items.WEATHERED_CUT_COPPER, "weathered_cut_copper");
        registerSlabToBlock(exporter, Items.OXIDIZED_CUT_COPPER_SLAB, Items.OXIDIZED_CUT_COPPER, "oxidized_cut_copper");
        registerSlabToBlock(exporter, Items.WAXED_CUT_COPPER_SLAB, Items.WAXED_CUT_COPPER, "waxed_cut_copper");
        registerSlabToBlock(exporter, Items.WAXED_EXPOSED_CUT_COPPER_SLAB, Items.WAXED_EXPOSED_CUT_COPPER, "waxed_exposed_cut_copper");
        registerSlabToBlock(exporter, Items.WAXED_WEATHERED_CUT_COPPER_SLAB, Items.WAXED_WEATHERED_CUT_COPPER, "waxed_weathered_cut_copper");
        registerSlabToBlock(exporter, Items.WAXED_OXIDIZED_CUT_COPPER_SLAB, Items.WAXED_OXIDIZED_CUT_COPPER, "waxed_oxidized_cut_copper");

        // Mod Slabs
        registerSlabToBlock(exporter, ModBlocks.PALE_OAK_SLAB.get().asItem(), ModBlocks.PALE_OAK_PLANKS.get().asItem(), "pale_oak_planks");
        registerSlabToBlock(exporter, ModBlocks.RESIN_BRICK_SLAB.get().asItem(), ModBlocks.RESIN_BRICKS.get().asItem(), "resin_bricks");

        ModBlocks.WOOD_SETS.forEach((setName, set) -> {
            registerSlabToBlock(exporter, set.slab().get().asItem(), set.planks().get().asItem(), setName + "_planks");
        });
    }

    private void addStonecutterRecipes(RecipeOutput exporter) {
        // --- Color set concrete → plate / edge / beam / pergola / geowindow / bannister / support ---
        ModBlocks.COLOR_SETS.forEach((color, set) -> {
            stonecutOne(exporter, set.concrete().get(), set.plate().get(),           color + "_plate_stonecutting");
            stonecutOne(exporter, set.concrete().get(), set.edge().get(),            color + "_edge_stonecutting");
            stonecutOne(exporter, set.concrete().get(), set.beam().get(),            color + "_beam_stonecutting");
            stonecutOne(exporter, set.concrete().get(), set.pergola().get(),         color + "_pergola_stonecutting");
            stonecutOne(exporter, set.concrete().get(), set.geometricWindow().get(), color + "_geowindow_stonecutting");
            stonecutOne(exporter, set.concrete().get(), set.bannister().get(),       color + "_bannister_stonecutting");
            stonecutOne(exporter, set.concrete().get(), set.supportSlab().get(),     color + "_support_slab_stonecutting");
            stonecutOne(exporter, set.concrete().get(), set.supportBeam().get(),     color + "_support_beam_stonecutting");
        });

        // (wood set planks → structural shapes moved to woodcutter below)

        // --- Stone set (input block → 8 architectural shapes) ---
        ModBlocks.STONE_SETS.forEach((name, set) -> {
            com.otterly76.ott.block.stone.ModStoneVariants.StoneVariant v =
                    com.otterly76.ott.block.stone.ModStoneVariants.byName(name);
            if (v == null) return;
            ItemLike src = v.input().get();
            stonecutOne(exporter, src, set.plate().get(),            name + "_plate_stonecutting");
            stonecutOne(exporter, src, set.edge().get(),             name + "_edge_stonecutting");
            stonecutOne(exporter, src, set.beam().get(),             name + "_beam_stonecutting");
            stonecutOne(exporter, src, set.pergola().get(),          name + "_pergola_stonecutting");
            stonecutOne(exporter, src, set.geometricWindow().get(),  name + "_geowindow_stonecutting");
            stonecutOne(exporter, src, set.bannister().get(),        name + "_bannister_stonecutting");
            stonecutOne(exporter, src, set.supportSlab().get(),      name + "_support_slab_stonecutting");
            stonecutOne(exporter, src, set.supportBeam().get(),      name + "_support_beam_stonecutting");
        });

        // --- Static blocks with known shape variants ---
        stonecutOne(exporter, ModBlocks.LIMESTONE_MASONRY.get(),                         ModBlocks.LIMESTONE_MASONRY_EDGE.get(),                        "limestone_masonry_edge_stonecutting");
        stonecutOne(exporter, ModBlocks.LIMESTONE_MASONRY.get(),                         ModBlocks.LIMESTONE_MASONRY_PLATE.get(),                       "limestone_masonry_plate_stonecutting");
        woodcutOne(exporter, ModBlocks.WHEAT_THATCH.get(),                              ModBlocks.WHEAT_THATCH_EDGE.get(),                             "wheat_thatch_edge_woodcutting");
        woodcutOne(exporter, ModBlocks.WHEAT_THATCH.get(),                              ModBlocks.WHEAT_THATCH_PLATE.get(),                            "wheat_thatch_plate_woodcutting");
        woodcutOne(exporter, ModBlocks.BAMBOO_THATCH.get(),                             ModBlocks.BAMBOO_THATCH_EDGE.get(),                            "bamboo_thatch_edge_woodcutting");
        woodcutOne(exporter, ModBlocks.BAMBOO_THATCH.get(),                             ModBlocks.BAMBOO_THATCH_PLATE.get(),                           "bamboo_thatch_plate_woodcutting");
        // --- Sandstone slender from smooth sandstone (base blocks only; all shapes via stone set) ---
        stonecutOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SANDSTONE_SLENDER_BRICKS.get(),            "sandstone_slender_bricks_from_smooth_sandstone_stonecutting");
        stonecutOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SANDSTONE_SLENDER_TURQUOISE_PATTERN.get(), "sandstone_slender_turquoise_pattern_from_smooth_sandstone_stonecutting");
        // --- Sandstone_crenelation from smooth sandstone ---
        stonecutOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SANDSTONE_CRENELATION.get(),          "sandstone_crenelation_from_smooth_sandstone_stonecutting");
        // --- Stone bricks masonry from stone ---
        stonecutOne(exporter, Blocks.STONE, ModBlocks.STONE_BRICKS_MASONRY.get(),       "stone_bricks_masonry_stonecutting");
        stonecutOne(exporter, Blocks.STONE, ModBlocks.STONE_BRICKS_MASONRY_EDGE.get(),  "stone_bricks_masonry_edge_stonecutting");
        stonecutOne(exporter, Blocks.STONE, ModBlocks.STONE_BRICKS_MASONRY_PLATE.get(), "stone_bricks_masonry_plate_stonecutting");
        // --- Stone bricks functional blocks from stone bricks ---
        stonecutOne(exporter, Blocks.STONE_BRICKS, ModBlocks.STONE_BRICKS_ARROWSLIT.get(),   "stone_bricks_arrowslit_stonecutting");
        stonecutOne(exporter, Blocks.STONE_BRICKS, ModBlocks.STONE_BRICKS_MACHICOLATION.get(),"stone_bricks_machicolation_stonecutting");
        stonecutOne(exporter, Blocks.STONE_BRICKS, ModBlocks.STONE_BRICKS_POOL.get(),         "stone_bricks_pool_stonecutting");
        stonecutOne(exporter, Blocks.STONE_BRICKS, ModBlocks.STONE_BRICKS_SMALL_POOL.get(),   "stone_bricks_small_pool_stonecutting");
        // --- Limestone stonecutter source ---
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.WATER_MOSAIC_TRADITIONAL.get(), "water_mosaic_traditional_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.WATER_MOSAIC_BORDER.get(),      "water_mosaic_border_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.WATER_MOSAIC_GEOMETRIC.get(),   "water_mosaic_geometric_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.WATER_MOSAIC_PATTERN.get(),     "water_mosaic_pattern_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.WATER_MOSAIC_DELICATE.get(),    "water_mosaic_delicate_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.WATER_MOSAIC_RECESS.get(),      "water_mosaic_recess_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.SPIRIT_MOSAIC_RECESS.get(),     "spirit_mosaic_recess_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.EARTH_MOSAIC_RECESS.get(),      "earth_mosaic_recess_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.FIRE_MOSAIC_RECESS.get(),       "fire_mosaic_recess_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.EARTH_MOSAIC_TRADITIONAL.get(), "earth_mosaic_traditional_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.EARTH_MOSAIC_BORDER.get(),      "earth_mosaic_border_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.EARTH_MOSAIC_GEOMETRIC.get(),   "earth_mosaic_geometric_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.EARTH_MOSAIC_PATTERN.get(),     "earth_mosaic_pattern_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.EARTH_MOSAIC_DELICATE.get(),    "earth_mosaic_delicate_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.FIRE_MOSAIC_TRADITIONAL.get(),  "fire_mosaic_traditional_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.FIRE_MOSAIC_BORDER.get(),       "fire_mosaic_border_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.FIRE_MOSAIC_GEOMETRIC.get(),    "fire_mosaic_geometric_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.FIRE_MOSAIC_PATTERN.get(),      "fire_mosaic_pattern_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.FIRE_MOSAIC_DELICATE.get(),     "fire_mosaic_delicate_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.SPIRIT_MOSAIC_TRADITIONAL.get(), "spirit_mosaic_traditional_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.SPIRIT_MOSAIC_BORDER.get(),     "spirit_mosaic_border_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.SPIRIT_MOSAIC_GEOMETRIC.get(),  "spirit_mosaic_geometric_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.SPIRIT_MOSAIC_PATTERN.get(),    "spirit_mosaic_pattern_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.SPIRIT_MOSAIC_DELICATE.get(),   "spirit_mosaic_delicate_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.AIR_MOSAIC_RECESS.get(),        "air_mosaic_recess_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.AIR_MOSAIC_TRADITIONAL.get(),   "air_mosaic_traditional_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.AIR_MOSAIC_BORDER.get(),        "air_mosaic_border_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.AIR_MOSAIC_GEOMETRIC.get(),     "air_mosaic_geometric_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.AIR_MOSAIC_PATTERN.get(),       "air_mosaic_pattern_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.AIR_MOSAIC_DELICATE.get(),      "air_mosaic_delicate_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.MOSAIC_FLOOR.get(),             "mosaic_floor_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.MOSAIC_FLOOR_DELICATE.get(),    "mosaic_floor_delicate_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.MOSAIC_FLOOR_ROSETTE.get(),     "mosaic_floor_rosette_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.ROMAN_FRESCO_RED.get(),         "roman_fresco_red_from_limestone_stonecutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.ROMAN_FRESCO_BLACK.get(),       "roman_fresco_black_from_limestone_stonecutting");
        // --- Limestone masonry from limestone ---
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.LIMESTONE_MASONRY.get(), "limestone_masonry_from_limestone_stonecutting");
        // --- Black marble stonecutting ---
        stonecutOne(exporter,  ModBlocks.BLACK_MARBLE.get(), ModBlocks.BLACK_MARBLE_BRICKS.get(),       "black_marble_bricks_stonecutting");
        stonecutOne(exporter,  ModBlocks.BLACK_MARBLE.get(), ModBlocks.BLACK_MARBLE_SMALL_BRICKS.get(), "black_marble_small_bricks_stonecutting");
        stonecutOne(exporter,  ModBlocks.BLACK_MARBLE.get(), ModBlocks.BLACK_MARBLE_TILES.get(),        "black_marble_tiles_stonecutting");
        stonecutOne(exporter,  ModBlocks.BLACK_MARBLE.get(), ModBlocks.BLACK_MARBLE_PILLAR.get(),       "black_marble_pillar_stonecutting");
        stonecutOne(exporter,  ModBlocks.BLACK_MARBLE.get(), ModBlocks.BLACK_MARBLE_PILLAR_CAP.get(),   "black_marble_pillar_cap_stonecutting");
        stonecutOne(exporter,  ModBlocks.BLACK_MARBLE.get(), ModBlocks.BLACK_MARBLE_FANCY_FENCE.get(),  "black_marble_fancy_fence_stonecutting");
        stonecutMany(exporter, ModBlocks.BLACK_MARBLE.get(), ModBlocks.BLACK_MARBLE_FLOOR_TILE.get(), "black_marble_floor_tile_stonecutting");
        // --- White marble stonecutting ---
        stonecutOne(exporter,  ModBlocks.WHITE_MARBLE.get(), ModBlocks.WHITE_MARBLE_BRICKS.get(),       "white_marble_bricks_stonecutting");
        stonecutOne(exporter,  ModBlocks.WHITE_MARBLE.get(), ModBlocks.WHITE_MARBLE_SMALL_BRICKS.get(), "white_marble_small_bricks_stonecutting");
        stonecutOne(exporter,  ModBlocks.WHITE_MARBLE.get(), ModBlocks.WHITE_MARBLE_TILES.get(),        "white_marble_tiles_stonecutting");
        stonecutOne(exporter,  ModBlocks.WHITE_MARBLE.get(), ModBlocks.WHITE_MARBLE_PILLAR.get(),       "white_marble_pillar_stonecutting");
        stonecutOne(exporter,  ModBlocks.WHITE_MARBLE.get(), ModBlocks.WHITE_MARBLE_PILLAR_CAP.get(),   "white_marble_pillar_cap_stonecutting");
        stonecutOne(exporter,  ModBlocks.WHITE_MARBLE.get(), ModBlocks.WHITE_MARBLE_FANCY_FENCE.get(),  "white_marble_fancy_fence_stonecutting");
        stonecutMany(exporter, ModBlocks.WHITE_MARBLE.get(), ModBlocks.WHITE_MARBLE_FLOOR_TILE.get(), "white_marble_floor_tile_stonecutting");
        woodcutOne(exporter, Items.SPRUCE_PLANKS, ModBlocks.SPRUCE_LOG_FENCE.get(), "spruce_log_fence_woodcutting");
        stonecutOne(exporter, ModBlocks.PLAIN_LIMESTONE.get(), ModBlocks.MIXED_LIMESTONE_BRICKS.get(), "mixed_limestone_bricks_from_limestone_stonecutting");
        // --- Chiseled plastered stone from matching plastered stone ---
        ModBlocks.PATTERN_BLOCKS.get("plastered_stone").forEach((color, base) -> {
            var chiseled = ModBlocks.PATTERN_BLOCKS.get("chiseled_plastered_stone");
            if (chiseled != null && chiseled.containsKey(color))
                stonecutOne(exporter, base.get(), chiseled.get(color).get(), color + "_chiseled_plastered_stone_stonecutting");
        });
        // --- All wood structural shapes: ott wood sets (planks + log + wood + stripped) ---
        ModBlocks.WOOD_SETS.forEach((name, set) -> {
            woodcutStructural(exporter, set.planks().get(),       name + "_planks",       set);
            woodcutStructural(exporter, set.log().get(),          name + "_log",          set);
            woodcutStructural(exporter, set.wood().get(),         name + "_wood",         set);
            woodcutStructural(exporter, set.strippedLog().get(),  name + "_stripped_log", set);
            woodcutStructural(exporter, set.strippedWood().get(), name + "_stripped_wood",set);
        });
        // --- All wood structural shapes: vanilla + pale oak structural sets ---
        ModBlocks.VANILLA_STRUCTURAL_SETS.forEach((name, set) -> {
            switch (name) {
                case "pale_oak" -> {
                woodcutStructural(exporter, ModBlocks.PALE_OAK_PLANKS.get(),        name + "_planks",       set);
                woodcutStructural(exporter, ModBlocks.PALE_OAK_LOG.get(),           name + "_log",          set);
                woodcutStructural(exporter, ModBlocks.PALE_OAK_WOOD.get(),          name + "_wood",         set);
                woodcutStructural(exporter, ModBlocks.STRIPPED_PALE_OAK_LOG.get(),  name + "_stripped_log", set);
                woodcutStructural(exporter, ModBlocks.STRIPPED_PALE_OAK_WOOD.get(), name + "_stripped_wood",set);
                }
                case "bamboo" -> {
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace("bamboo_planks"))
                        .ifPresent(b -> woodcutStructural(exporter, b, "bamboo_planks", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace("bamboo_block"))
                        .ifPresent(b -> woodcutStructural(exporter, b, "bamboo_block", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace("stripped_bamboo_block"))
                        .ifPresent(b -> woodcutStructural(exporter, b, "stripped_bamboo_block", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace("bamboo_mosaic"))
                        .ifPresent(b -> woodcutStructural(exporter, b, "bamboo_mosaic", set));
                }
                case "crimson", "warped" -> {
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace(name + "_planks"))
                        .ifPresent(b -> woodcutStructural(exporter, b, name + "_planks", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace(name + "_stem"))
                        .ifPresent(b -> woodcutStructural(exporter, b, name + "_stem", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace(name + "_hyphae"))
                        .ifPresent(b -> woodcutStructural(exporter, b, name + "_hyphae", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace("stripped_" + name + "_stem"))
                        .ifPresent(b -> woodcutStructural(exporter, b, "stripped_" + name + "_stem", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace("stripped_" + name + "_hyphae"))
                        .ifPresent(b -> woodcutStructural(exporter, b, "stripped_" + name + "_hyphae", set));
                }
                default -> {
                // Regular overworld wood (oak, spruce, birch, jungle, acacia, dark_oak, mangrove, cherry)
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace(name + "_planks"))
                        .ifPresent(b -> woodcutStructural(exporter, b, name + "_planks", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace(name + "_log"))
                        .ifPresent(b -> woodcutStructural(exporter, b, name + "_log", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace(name + "_wood"))
                        .ifPresent(b -> woodcutStructural(exporter, b, name + "_wood", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace("stripped_" + name + "_log"))
                        .ifPresent(b -> woodcutStructural(exporter, b, "stripped_" + name + "_log", set));
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.withDefaultNamespace("stripped_" + name + "_wood"))
                        .ifPresent(b -> woodcutStructural(exporter, b, "stripped_" + name + "_wood", set));
                }
            }
        });
    }

    private void stonecutOne(RecipeOutput exporter, ItemLike input, ItemLike output, String id) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, 1)
                .unlockedBy("has_input", has(input))
                .save(exporter, getRecipePath("ott", id));
    }

    private void stonecutMany(RecipeOutput exporter, ItemLike input, ItemLike output, String id) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, 4)
                .unlockedBy("has_input", has(input))
                .save(exporter, getRecipePath("ott", id));
    }

    private void woodcutOne(RecipeOutput exporter, ItemLike input, ItemLike output, String id) {
        new SingleItemRecipeBuilder(RecipeCategory.BUILDING_BLOCKS, WoodcuttingRecipe::new, Ingredient.of(input), output, 1)
                .unlockedBy("has_input", has(input))
                .save(exporter, getRecipePath("ott", id));
    }

    private void engraveOne(RecipeOutput exporter, ItemLike input, ItemLike output, String id) {
        new SingleItemRecipeBuilder(RecipeCategory.BUILDING_BLOCKS, EngravingRecipe::new, Ingredient.of(input), output, 1)
                .unlockedBy("has_input", has(input))
                .save(exporter, getRecipePath("ott", id));
    }

    private void engraveTagged(RecipeOutput exporter, TagKey<Item> inputTag, ItemLike output, String id) {
        new SingleItemRecipeBuilder(RecipeCategory.BUILDING_BLOCKS, EngravingRecipe::new, Ingredient.of(inputTag), output, 1)
                .unlockedBy("has_input", has(inputTag))
                .save(exporter, getRecipePath("ott", id));
    }

    private void engraveRecipes(RecipeOutput exporter) {
        // ── Stone → engraved stone variants ──────────────────────────────────────
        engraveOne(exporter, Blocks.STONE, ModBlocks.ANGRY_STONE,                          "angry_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.BLANK_STONE_CARVING,                  "blank_stone_carving_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.BORDERED_STONE,                       "bordered_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.BRICK_BORDERED_STONE,                 "brick_bordered_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CARVED_STONE,                         "carved_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CHECKERED_STONE_TILES,                "checkered_stone_tiles_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.COBBLED_STONE,                        "cobbled_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CRACKED_DISORDERED_STONE_BRICKS,      "cracked_disordered_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CRACKED_FLAT_STONE_TILES,             "cracked_flat_stone_tiles_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CREEPER_STONE_CARVING,                "creeper_stone_carving_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CRYING_STONE,                         "crying_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CURLY_STONE_PILLAR,                   "curly_stone_pillar_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CUT_BLANK_STONE,                      "cut_blank_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CUT_STONE_COLUMN,                     "cut_stone_column_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.DUH_STONE,                            "duh_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.EDGED_STONE_BRICKS,                   "edged_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.ENGRAVED_STONE,                       "engraved_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.ETCHED_STONE_BRICKS,                  "etched_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.FINE_STONE_PILLAR,                    "fine_stone_pillar_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.FLAT_STONE_TILES,                     "flat_stone_tiles_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.GLAD_STONE,                           "glad_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.INLAYED_STONE,                        "inlayed_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.INSCRIBED_STONE,                      "inscribed_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.LAYED_STONE_BRICKS,                   "layed_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.LODED_STONE,                          "loded_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.MASSIVE_STONE_BRICKS,                 "massive_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.OFFSET_STONE_BRICKS,                  "offset_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.ORNATE_STONE_PILLAR,                  "ornate_stone_pillar_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.OVERLAPPING_STONE_TILES,              "overlapping_stone_tiles_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.PILLAR_STONE_BRICKS,                  "pillar_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.POLISHED_STONE,                       "polished_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.PRISMAL_STONE_REMNANTS,               "prismal_stone_remnants_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.ROUGH_STONE,                          "rough_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.ROUNDED_STONE_BRICKS,                 "rounded_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.RUNIC_CARVED_STONE,                   "runic_carved_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SAD_STONE,                            "sad_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SANDED_STONE,                         "sanded_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SIMPLE_STONE_PILLAR,                  "simple_stone_pillar_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SMALL_STONE_BRICKS,                   "small_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SMOOTH_INLAYED_STONE,                 "smooth_inlayed_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SMOOTH_STONE_COLUMN,                  "smooth_stone_column_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SMOOTH_STONE_CTM,                     "smooth_stone_ctm_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SMOOTHED_DOUBLE_INLAYED_STONE,        "smoothed_double_inlayed_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SPIDER_STONE_CARVING,                 "spider_stone_carving_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SPIRALED_STONE,                       "spiraled_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STACKED_STONE_BRICKS,                 "stacked_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_MINI_TILES,                     "stone_mini_tiles_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_PILLAR,                         "stone_pillar_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_SCALES,                         "stone_scales_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.THICK_INLAYED_STONE,                  "thick_inlayed_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TILED_BORDERED_STONE,                 "tiled_bordered_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TILED_STONE,                          "tiled_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TILED_STONE_COLUMN,                   "tiled_stone_column_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TINY_BRICK_BORDERED_STONE,            "tiny_brick_bordered_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TINY_LAYERED_STONE_BRICKS,            "tiny_layered_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TINY_LAYERED_STONE_SLABS,             "tiny_layered_stone_slabs_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TINY_STONE_BRICKS,                    "tiny_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TRODDEN_STONE,                        "trodden_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.UNAMUSED_STONE,                       "unamused_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.VERTICAL_CUT_STONE,                   "vertical_cut_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.VERTICAL_DISORDERED_STONE_BRICKS,     "vertical_disordered_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.WEATHERED_STONE,                      "weathered_stone_engraving");
        // ── New plain stone blocks ────────────────────────────────────────────────
        engraveOne(exporter, Blocks.STONE, ModBlocks.CHAOTIC_STONE_BRICKS,               "chaotic_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CHAOTIC_MEDIUM_STONE_BRICKS,        "chaotic_medium_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CHAOTIC_SMALL_STONE_BRICKS,         "chaotic_small_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.DIAMOND_STONE_PAVERS,               "diamond_stone_pavers_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.ENCASED_STONE_BRICKS,               "encased_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.FRENCH_STONE,                       "french_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.LARGE_ORNATE_STONE,                 "large_ornate_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.LARGE_STONE_TILE,                   "large_stone_tile_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.MESSY_STONE_TILES,                  "messy_stone_tiles_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.MOSAIC_STONE,                       "mosaic_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.NOTCHED_STONE_BRICKS,               "notched_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.ORNATE_STONE,                       "ornate_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.POISON_STONE,                       "poison_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.POLISHED_CUT_STONE,                 "polished_cut_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.POLISHED_STONE_TILES,               "polished_stone_tiles_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.PRISM_STONE,                        "prism_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SLANTED_STONE,                      "slanted_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_ARRAY,                        "stone_array_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_BRAID,                        "stone_braid_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_DENT,                         "stone_dent_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_JELLYBEAN,                    "stone_jellybean_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_LAYERS,                       "stone_layers_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_PANEL,                        "stone_panel_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_ROAD,                         "stone_road_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_ZAG,                          "stone_zag_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SUNKEN_STONE,                       "sunken_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.TRIPLE_STONE_BRICKS,                "triple_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.WEATHERED_STONE_BRICKS,             "weathered_stone_bricks_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.WEATHERED_TILED_STONE,              "weathered_tiled_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.WEAVER_STONE,                       "weaver_stone_engraving");
        // ── New carved/cut stone blocks ───────────────────────────────────────────
        engraveOne(exporter, Blocks.STONE, ModBlocks.CARVED_STONE_CREEPER,               "carved_stone_creeper_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CARVED_STONE_DERP,                  "carved_stone_derp_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CARVED_STONE_VILLAGER,              "carved_stone_villager_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CARVED_STONE_WITHER,                "carved_stone_wither_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CARVED_STONE_WRITING,               "carved_stone_writing_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.CUT_STONE,                          "cut_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.ROUGH_CUT_STONE,                    "rough_cut_stone_engraving");
        // ── New stone pillars ─────────────────────────────────────────────────────
        engraveOne(exporter, Blocks.STONE, ModBlocks.SHEARED_STONE_PILLAR,               "sheared_stone_pillar_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.SLATED_STONE,                       "slated_stone_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_COLUMN,                       "stone_column_engraving");
        engraveOne(exporter, Blocks.STONE, ModBlocks.STONE_TWISTING_COLUMN,              "stone_twisting_column_engraving");
        // ── Chisel pillar blocks ──────────────────────────────────────────────────
        ModBlocks.CHISEL_PILLARS.forEach((name, block) ->
                engraveOne(exporter, Blocks.STONE, block, name + "_engraving"));
        // ── Legend blocks ─────────────────────────────────────────────────────────
        engraveOne(exporter, Blocks.STONE, ModBlocks.CHISELED_STONE_BRICKS_LEGEND,       "chiseled_stone_bricks_legend_engraving");
        ModBlocks.CHISEL_LEGEND.forEach((name, block) ->
                engraveOne(exporter, Blocks.STONE, block, name + "_engraving"));

        // ── Stone bricks → masonry ────────────────────────────────────────────────
        engraveOne(exporter, Blocks.STONE_BRICKS, ModBlocks.STONE_BRICKS_MASONRY,          "stone_bricks_masonry_engraving");

        // ── Limestone → variants ──────────────────────────────────────────────────
        engraveOne(exporter, ModBlocks.PLAIN_LIMESTONE, ModBlocks.MIXED_LIMESTONE_BRICKS,  "mixed_limestone_bricks_engraving");
        engraveOne(exporter, ModBlocks.PLAIN_LIMESTONE, ModBlocks.LIMESTONE_MASONRY,       "limestone_masonry_engraving");

        // ── Smooth sandstone → mosaic + decorative ───────────────────────────────
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SANDSTONE_SLENDER_BRICKS,          "sandstone_slender_bricks_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SANDSTONE_SLENDER_TURQUOISE_PATTERN,"sandstone_slender_turquoise_pattern_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.AIR_MOSAIC_BORDER,                  "air_mosaic_border_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.AIR_MOSAIC_GEOMETRIC,               "air_mosaic_geometric_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.AIR_MOSAIC_PATTERN,                 "air_mosaic_pattern_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.AIR_MOSAIC_DELICATE,                "air_mosaic_delicate_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.AIR_MOSAIC_TRADITIONAL,             "air_mosaic_traditional_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.EARTH_MOSAIC_BORDER,                "earth_mosaic_border_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.EARTH_MOSAIC_GEOMETRIC,             "earth_mosaic_geometric_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.EARTH_MOSAIC_PATTERN,               "earth_mosaic_pattern_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.EARTH_MOSAIC_DELICATE,              "earth_mosaic_delicate_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.EARTH_MOSAIC_TRADITIONAL,           "earth_mosaic_traditional_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.FIRE_MOSAIC_BORDER,                 "fire_mosaic_border_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.FIRE_MOSAIC_GEOMETRIC,              "fire_mosaic_geometric_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.FIRE_MOSAIC_PATTERN,                "fire_mosaic_pattern_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.FIRE_MOSAIC_DELICATE,               "fire_mosaic_delicate_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.FIRE_MOSAIC_TRADITIONAL,            "fire_mosaic_traditional_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.WATER_MOSAIC_BORDER,                "water_mosaic_border_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.WATER_MOSAIC_GEOMETRIC,             "water_mosaic_geometric_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.WATER_MOSAIC_PATTERN,               "water_mosaic_pattern_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.WATER_MOSAIC_DELICATE,              "water_mosaic_delicate_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.WATER_MOSAIC_TRADITIONAL,           "water_mosaic_traditional_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SPIRIT_MOSAIC_BORDER,               "spirit_mosaic_border_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SPIRIT_MOSAIC_GEOMETRIC,            "spirit_mosaic_geometric_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SPIRIT_MOSAIC_PATTERN,              "spirit_mosaic_pattern_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SPIRIT_MOSAIC_DELICATE,             "spirit_mosaic_delicate_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.SPIRIT_MOSAIC_TRADITIONAL,          "spirit_mosaic_traditional_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.MOSAIC_FLOOR,                       "mosaic_floor_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.MOSAIC_FLOOR_DELICATE,              "mosaic_floor_delicate_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.MOSAIC_FLOOR_ROSETTE,               "mosaic_floor_rosette_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.ROMAN_FRESCO_RED,                   "roman_fresco_red_engraving");
        engraveOne(exporter, Blocks.SMOOTH_SANDSTONE, ModBlocks.ROMAN_FRESCO_BLACK,                 "roman_fresco_black_engraving");

        // ── Vanilla CTM blocks (one CTM variant each) ────────────────────────────
        engraveOne(exporter, Blocks.PURPUR_BLOCK,          ModBlocks.PURPUR_PILLAR_CTM,        "purpur_pillar_ctm_engraving");
        engraveOne(exporter, Blocks.SANDSTONE,             ModBlocks.SANDSTONE_CTM,            "sandstone_ctm_engraving");
        engraveOne(exporter, Blocks.RED_SANDSTONE,         ModBlocks.RED_SANDSTONE_CTM,        "red_sandstone_ctm_engraving");
        engraveOne(exporter, Blocks.ANDESITE,              ModBlocks.POLISHED_ANDESITE_CTM,    "polished_andesite_ctm_engraving");
        engraveOne(exporter, Blocks.BLACKSTONE,            ModBlocks.POLISHED_BLACKSTONE_CTM,  "polished_blackstone_ctm_engraving");
        engraveOne(exporter, Blocks.DIORITE,               ModBlocks.POLISHED_DIORITE_CTM,     "polished_diorite_ctm_engraving");
        engraveOne(exporter, Blocks.GRANITE,               ModBlocks.POLISHED_GRANITE_CTM,     "polished_granite_ctm_engraving");
        engraveOne(exporter, Blocks.NETHERITE_BLOCK,      ModBlocks.NETHERITE_BLOCK_CTM,      "netherite_block_ctm_engraving");
        engraveOne(exporter, Blocks.NETHERITE_BLOCK,      ModBlocks.NETHERITE_BLOCK_PILLAR,   "netherite_block_pillar_engraving");
        engraveOne(exporter, Blocks.EMERALD_BLOCK,        ModBlocks.EMERALD_BLOCK_CTM,        "emerald_block_ctm_engraving");
        engraveOne(exporter, ModBlocks.EMERALD_BLOCK_CTM, Blocks.EMERALD_BLOCK,               "emerald_block_from_ctm_engraving");

        // ── Vanilla pillar CTM blocks ────────────────────────────────────────────
        engraveOne(exporter, Blocks.BONE_BLOCK,           ModBlocks.BONE_BLOCK_PILLAR,        "bone_block_pillar_engraving");
        engraveOne(exporter, Blocks.COAL_BLOCK,           ModBlocks.COAL_BLOCK_PILLAR,        "coal_block_pillar_engraving");
        engraveOne(exporter, Blocks.COBBLED_DEEPSLATE,    ModBlocks.COBBLED_DEEPSLATE_PILLAR, "cobbled_deepslate_pillar_engraving");
        engraveOne(exporter, Blocks.COBBLESTONE,          ModBlocks.COBBLESTONE_PILLAR,       "cobblestone_pillar_engraving");
        engraveOne(exporter, Blocks.COPPER_BLOCK,         ModBlocks.COPPER_BLOCK_PILLAR,      "copper_block_pillar_engraving");
        engraveOne(exporter, Blocks.LAPIS_BLOCK,          ModBlocks.LAPIS_BLOCK_PILLAR,       "lapis_block_pillar_engraving");
        engraveOne(exporter, Blocks.OBSIDIAN,             ModBlocks.OBSIDIAN_PILLAR,          "obsidian_pillar_engraving");

        // ── White marble → variants ───────────────────────────────────────────────
        engraveOne(exporter, ModBlocks.WHITE_MARBLE, ModBlocks.WHITE_MARBLE_BRICKS,       "white_marble_bricks_engraving");
        engraveOne(exporter, ModBlocks.WHITE_MARBLE, ModBlocks.WHITE_MARBLE_SMALL_BRICKS, "white_marble_small_bricks_engraving");
        engraveOne(exporter, ModBlocks.WHITE_MARBLE, ModBlocks.WHITE_MARBLE_TILES,        "white_marble_tiles_engraving");
        engraveOne(exporter, ModBlocks.WHITE_MARBLE, ModBlocks.WHITE_POLISHED_MARBLE,     "white_polished_marble_engraving");
        engraveOne(exporter, ModBlocks.WHITE_MARBLE, ModBlocks.WHITE_MARBLE_PILLAR,       "white_marble_pillar_engraving");
        engraveOne(exporter, ModBlocks.WHITE_MARBLE, ModBlocks.WHITE_MARBLE_PILLAR_CAP,   "white_marble_pillar_cap_engraving");

        // ── Black marble → variants ───────────────────────────────────────────────
        engraveOne(exporter, ModBlocks.BLACK_MARBLE, ModBlocks.BLACK_MARBLE_BRICKS,       "black_marble_bricks_engraving");
        engraveOne(exporter, ModBlocks.BLACK_MARBLE, ModBlocks.BLACK_MARBLE_SMALL_BRICKS, "black_marble_small_bricks_engraving");
        engraveOne(exporter, ModBlocks.BLACK_MARBLE, ModBlocks.BLACK_MARBLE_TILES,        "black_marble_tiles_engraving");
        engraveOne(exporter, ModBlocks.BLACK_MARBLE, ModBlocks.BLACK_POLISHED_MARBLE,     "black_polished_marble_engraving");
        engraveOne(exporter, ModBlocks.BLACK_MARBLE, ModBlocks.BLACK_MARBLE_PILLAR,       "black_marble_pillar_engraving");
        engraveOne(exporter, ModBlocks.BLACK_MARBLE, ModBlocks.BLACK_MARBLE_PILLAR_CAP,   "black_marble_pillar_cap_engraving");

        // ── White plastered stone → decorative variants ──────────────────────────
        Block whitePlastered = ModBlocks.PATTERN_BLOCKS.get("plastered_stone").get("white").get();
        engraveOne(exporter, whitePlastered,
                ModBlocks.PATTERN_BLOCKS.get("gilded_plastered_stone").get("white").get(),   "white_gilded_plastered_stone_engraving");
        engraveOne(exporter, whitePlastered,
                ModBlocks.PATTERN_BLOCKS.get("delicate_plastered_stone").get("white").get(), "white_delicate_plastered_stone_engraving");
        engraveOne(exporter, whitePlastered,
                ModBlocks.PATTERN_BLOCKS.get("banded_plastered_stone").get("white").get(),   "white_banded_plastered_stone_engraving");
        engraveOne(exporter, whitePlastered,
                ModBlocks.CHISELED_PLASTERED_STONE_PILLAR,                                   "chiseled_plastered_stone_pillar_engraving");

        // ── Wool → ornamented / delicate variants ─────────────────────────────────
        engraveOne(exporter, Blocks.RED_WOOL,    ModBlocks.ORNAMENTED_RED_WOOL,    "ornamented_red_wool_engraving");
        engraveOne(exporter, Blocks.RED_WOOL,    ModBlocks.DELICATE_RED_WOOL,      "delicate_red_wool_engraving");
        engraveOne(exporter, Blocks.BLUE_WOOL,   ModBlocks.ORNAMENTED_BLUE_WOOL,   "ornamented_blue_wool_engraving");
        engraveOne(exporter, Blocks.BLUE_WOOL,   ModBlocks.DELICATE_BLUE_WOOL,     "delicate_blue_wool_engraving");
        engraveOne(exporter, Blocks.GREEN_WOOL,  ModBlocks.ORNAMENTED_GREEN_WOOL,  "ornamented_green_wool_engraving");
        engraveOne(exporter, Blocks.GREEN_WOOL,  ModBlocks.DELICATE_GREEN_WOOL,    "delicate_green_wool_engraving");
        engraveOne(exporter, Blocks.PURPLE_WOOL, ModBlocks.ORNAMENTED_PURPLE_WOOL, "ornamented_purple_wool_engraving");
        engraveOne(exporter, Blocks.PURPLE_WOOL, ModBlocks.DELICATE_PURPLE_WOOL,   "delicate_purple_wool_engraving");

        // ── Wood door variants ────────────────────────────────────────────────────
        ModBlocks.WOOD_DOORS.forEach((wood, styleMap) -> {
            TagKey<Item> woodDoorsTag = TagKey.create(Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "material/" + wood));
            styleMap.forEach((style, block) ->
                    engraveTagged(exporter, woodDoorsTag, block.get(), style + "_" + wood + "_door_engraving")
            );
        });
    }

    private void woodcutStructural(RecipeOutput exporter, ItemLike input, String prefix, ModBlocks.WoodSetBlocks set) {
        woodcutOne(exporter, input, set.planksPlate().get(),     prefix + "_plate_woodcutting");
        woodcutOne(exporter, input, set.planksEdge().get(),      prefix + "_edge_woodcutting");
        woodcutOne(exporter, input, set.beam().get(),            prefix + "_beam_woodcutting");
        woodcutOne(exporter, input, set.pergola().get(),         prefix + "_pergola_woodcutting");
        woodcutOne(exporter, input, set.geometricWindow().get(), prefix + "_geowindow_woodcutting");
        woodcutOne(exporter, input, set.bannister().get(),       prefix + "_bannister_woodcutting");
        woodcutOne(exporter, input, set.supportSlab().get(),     prefix + "_support_slab_woodcutting");
        woodcutOne(exporter, input, set.supportBeam().get(),     prefix + "_support_beam_woodcutting");
    }

    private void woodcutStructural(RecipeOutput exporter, ItemLike input, String prefix, ModBlocks.WoodStructuralBlocks set) {
        woodcutOne(exporter, input, set.planksPlate().get(),     prefix + "_plate_woodcutting");
        woodcutOne(exporter, input, set.planksEdge().get(),      prefix + "_edge_woodcutting");
        woodcutOne(exporter, input, set.beam().get(),            prefix + "_beam_woodcutting");
        woodcutOne(exporter, input, set.pergola().get(),         prefix + "_pergola_woodcutting");
        woodcutOne(exporter, input, set.geometricWindow().get(), prefix + "_geowindow_woodcutting");
        woodcutOne(exporter, input, set.bannister().get(),       prefix + "_bannister_woodcutting");
        woodcutOne(exporter, input, set.supportSlab().get(),     prefix + "_support_slab_woodcutting");
        woodcutOne(exporter, input, set.supportBeam().get(),     prefix + "_support_beam_woodcutting");
    }

    private void addOpalSetRecipes(@NotNull RecipeOutput exporter, @NotNull String name,
                                   @NotNull ModBlocks.OpalSet set, @NotNull Item crystal) {
        // 4 crystals in 2×2 → 1 base block
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.base().get())
                .define('C', crystal)
                .pattern("CC")
                .pattern("CC")
                .unlockedBy("has_crystal", has(crystal))
                .save(exporter, getRecipePath("ott", name + "_from_crystals"));

        // base → 4 crystals (shapeless decompose)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, crystal, 4)
                .requires(set.base().get())
                .unlockedBy("has_" + name, has(set.base().get()))
                .save(exporter, getRecipePath("ott", name + "_decompose"));

        // 4 crystals in 2×2 → 1 crystal_block (storage block)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.crystalBlock().get())
                .define('B', set.base().get())
                .pattern("BB")
                .pattern("BB")
                .unlockedBy("has_" + name, has(set.base().get()))
                .save(exporter, getRecipePath("ott", name + "_crystal_block"));

        // ── Stonecutter cascade ──────────────────────────────────────────────
        // From base: every decorative variant
        stonecutOne(exporter, set.base().get(), set.polished().get(),   "polished_" + name + "_from_base");
        stonecutOne(exporter, set.base().get(), set.cut().get(),        "cut_" + name + "_from_base");
        stonecutOne(exporter, set.base().get(), set.bricks().get(),     name + "_bricks_from_base");
        stonecutOne(exporter, set.base().get(), set.smallBricks().get(),"small_" + name + "_bricks_from_base");
        stonecutOne(exporter, set.base().get(), set.chiseled().get(),   "chiseled_" + name + "_from_base");
        stonecutOne(exporter, set.base().get(), set.pillar().get(),     name + "_pillar_from_base");
        stonecutOne(exporter, set.base().get(), set.tiles().get(),      name + "_tiles_from_base");
        stonecutOne(exporter, set.base().get(), set.smallTiles().get(), "small_" + name + "_tiles_from_base");
        stonecutOne(exporter, set.base().get(), set.tiling().get(),     name + "_tiling_from_base");
        // From polished
        stonecutOne(exporter, set.polished().get(), set.cut().get(),        "cut_" + name + "_from_polished");
        stonecutOne(exporter, set.polished().get(), set.bricks().get(),     name + "_bricks_from_polished");
        stonecutOne(exporter, set.polished().get(), set.smallBricks().get(),"small_" + name + "_bricks_from_polished");
        stonecutOne(exporter, set.polished().get(), set.chiseled().get(),   "chiseled_" + name + "_from_polished");
        stonecutOne(exporter, set.polished().get(), set.pillar().get(),     name + "_pillar_from_polished");
        stonecutOne(exporter, set.polished().get(), set.tiles().get(),      name + "_tiles_from_polished");
        stonecutOne(exporter, set.polished().get(), set.smallTiles().get(), "small_" + name + "_tiles_from_polished");
        stonecutOne(exporter, set.polished().get(), set.tiling().get(),     name + "_tiling_from_polished");
        // From cut
        stonecutOne(exporter, set.cut().get(), set.bricks().get(),     name + "_bricks_from_cut");
        stonecutOne(exporter, set.cut().get(), set.smallBricks().get(),"small_" + name + "_bricks_from_cut");
        stonecutOne(exporter, set.cut().get(), set.chiseled().get(),   "chiseled_" + name + "_from_cut");
        stonecutOne(exporter, set.cut().get(), set.tiles().get(),      name + "_tiles_from_cut");
        stonecutOne(exporter, set.cut().get(), set.smallTiles().get(), "small_" + name + "_tiles_from_cut");
        stonecutOne(exporter, set.cut().get(), set.tiling().get(),     name + "_tiling_from_cut");
        // From bricks
        stonecutOne(exporter, set.bricks().get(), set.smallBricks().get(), "small_" + name + "_bricks_from_bricks");
        stonecutOne(exporter, set.bricks().get(), set.tiles().get(),       name + "_tiles_from_bricks");
        stonecutOne(exporter, set.bricks().get(), set.smallTiles().get(),  "small_" + name + "_tiles_from_bricks");
        stonecutOne(exporter, set.bricks().get(), set.tiling().get(),      name + "_tiling_from_bricks");
        // From tiles
        stonecutOne(exporter, set.tiles().get(), set.smallTiles().get(), "small_" + name + "_tiles_from_tiles");
        stonecutOne(exporter, set.tiles().get(), set.tiling().get(),     name + "_tiling_from_tiles");
        // From small_tiles
        stonecutOne(exporter, set.smallTiles().get(), set.tiling().get(), name + "_tiling_from_small_tiles");

        // crystal → 1 base opal block (smelting)
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(crystal),
                        RecipeCategory.BUILDING_BLOCKS,
                        set.base().get(),
                        0.1F, 200)
                .unlockedBy("has_" + name + "_crystal", has(crystal))
                .save(exporter, getRecipePath("ott", name + "_from_crystal_smelting"));

        // glass: smelt polished → 1 opal glass
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(set.polished().get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        set.glass().get(),
                        0.1F, 200)
                .unlockedBy("has_polished_" + name, has(set.polished().get()))
                .save(exporter, getRecipePath("ott", name + "_glass_from_smelting"));

        // ── Engraving (base block → each decorative variant, like stone → stone variants) ──
        java.util.List<net.minecraft.world.level.block.Block> decorative = java.util.List.of(
                set.polished().get(), set.cut().get(), set.bricks().get(), set.smallBricks().get(),
                set.chiseled().get(), set.pillar().get(), set.tiles().get(), set.smallTiles().get(),
                set.glass().get(), set.glassPane().get(), set.tiling().get()
        );
        for (net.minecraft.world.level.block.Block output : decorative) {
            String outputPath = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(output).getPath();
            engraveOne(exporter, set.base().get(), output, name + "_to_" + outputPath);
        }
        // ── Engraving (all decorative variants convertible 1:1 between each other) ──
        for (net.minecraft.world.level.block.Block input : decorative) {
            String inputPath = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(input).getPath();
            for (net.minecraft.world.level.block.Block output : decorative) {
                if (input != output) {
                    String outputPath = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(output).getPath();
                    engraveOne(exporter, input, output, inputPath + "_to_" + outputPath);
                }
            }
        }
    }
}