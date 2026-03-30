package com.otterly76.ott.generation;

import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import com.otterly76.ott.Constants;
import com.otterly76.ott.color.ModColorSets;
import com.otterly76.ott.block.IGradientBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.util.ModTags;
import com.otterly76.ott.recipe.BundleColoring;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CREAKING_HEART.get())
                .define('#', ModBlocks.PALE_OAK_LOG.get())
                .define('O', ModBlocks.RESIN_BLOCK.get())
                .pattern("#")
                .pattern("O")
                .pattern("#")
                .unlockedBy("impossible", impossible())
                .save(noAdv, getRecipePath("minecraft", "creaking_heart"));

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
        this.addSlabToBlockRecipes(noAdv);
        this.addMiscRecipes(noAdv);

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
            Item result = ModItems.CUSTOM_DYES.get(colorSet.name()).get();
            Item ingredient1 = vanillaDyes.get(i);
            Item ingredient2 = vanillaDyes.get((i + 1) % vanillaDyes.size());

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 2)
                    .requires(ingredient1)
                    .requires(ingredient2)
                    .unlockedBy("impossible", impossible())
                    .save(exporter, getRecipePath(Constants.MOD_ID, colorSet.name() + "_dye"));
        }
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

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.GLOWSTONE), RecipeCategory.BUILDING_BLOCKS, ModBlocks.SMOOTH_GLOWSTONE.get(), 0.1F, 200)
                .unlockedBy("has_glowstone", has(Items.GLOWSTONE))
                .save(exporter, getRecipePath("ott", "smooth_glowstone_from_smelting"));
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

        // Salted Kelp
        // Catfish
        cooking(noAdv, List.of(ModItems.CATFISH.get()), ModItems.COOKED_CATFISH.get(), "cooked_catfish");
        // Bass
        cooking(noAdv, List.of(ModItems.BASS.get()), ModItems.COOKED_BASS.get(), "cooked_bass");

        // Buffalo
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
}