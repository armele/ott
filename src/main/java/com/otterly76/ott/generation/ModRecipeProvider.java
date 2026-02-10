package com.otterly76.ott.generation;

import com.otterly76.ott.block.IGradientBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.util.ModTags;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    }

    private void addMiscRecipes(RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.CHEST, 4)
                .define('#', ItemTags.LOGS)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(exporter, getRecipePath("ott", "chest_from_logs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.HOPPER)
                .define('I', Items.IRON_INGOT)
                .define('L', ItemTags.LOGS)
                .pattern("ILI")
                .pattern("ILI")
                .pattern(" I ")
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(exporter, getRecipePath("ott", "hopper_from_logs"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.TORCH_ARROW.get(), 8)
                .requires(Items.ARROW)
                .requires(Items.TORCH)
                .unlockedBy("has_arrow", has(Items.ARROW))
                .unlockedBy("has_torch", has(Items.TORCH))
                .save(exporter, getRecipePath("ott", "torch_arrow"));
    }

    private void addDyeingRecipes(RecipeOutput exporter) {
        for (DyeColor color : DyeColor.values()) {
            String colorName = color.getName();
            Item dye = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_dye"));

            // Banner
            Item banner = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_banner"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, banner)
                    .requires(ModTags.Items.DYEABLE_BANNERS)
                    .requires(dye)
                    .unlockedBy("has_any_banner", has(ModTags.Items.DYEABLE_BANNERS))
                    .save(exporter, getRecipePath("ott", colorName + "_banner_from_dyeing"));

            // Candle
            Item candle = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_candle"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, candle)
                    .requires(ModTags.Items.DYEABLE_CANDLES)
                    .requires(dye)
                    .unlockedBy("has_any_candle", has(ModTags.Items.DYEABLE_CANDLES))
                    .save(exporter, getRecipePath("ott", colorName + "_candle_from_dyeing"));

            // Glass
            Item glass = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_stained_glass"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, glass)
                    .requires(ModTags.Items.DYEABLE_GLASS_BLOCKS)
                    .requires(dye)
                    .unlockedBy("has_any_glass", has(ModTags.Items.DYEABLE_GLASS_BLOCKS))
                    .save(exporter, getRecipePath("ott", colorName + "_glass_from_dyeing"));

            // Pane
            Item pane = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_stained_glass_pane"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, pane)
                    .requires(ModTags.Items.DYEABLE_GLASS_PANES)
                    .requires(dye)
                    .unlockedBy("has_any_pane", has(ModTags.Items.DYEABLE_GLASS_PANES))
                    .save(exporter, getRecipePath("ott", colorName + "_pane_from_dyeing"));

            // Shulker Box
            Item shulker = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_shulker_box"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, shulker)
                    .requires(ModTags.Items.DYEABLE_SHULKER_BOXES)
                    .requires(dye)
                    .unlockedBy("has_any_shulker", has(ModTags.Items.DYEABLE_SHULKER_BOXES))
                    .save(exporter, getRecipePath("ott", colorName + "_shulker_box_from_dyeing"));

            // Concrete
            Item concrete = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_concrete"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, concrete)
                    .requires(ModTags.Items.DYEABLE_CONCRETE)
                    .requires(dye)
                    .unlockedBy("has_any_concrete", has(ModTags.Items.DYEABLE_CONCRETE))
                    .save(exporter, getRecipePath("ott", colorName + "_concrete_from_dyeing"));

            // Concrete Powder
            Item powder = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_concrete_powder"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, powder)
                    .requires(ModTags.Items.DYEABLE_CONCRETE_POWDER)
                    .requires(dye)
                    .unlockedBy("has_any_powder", has(ModTags.Items.DYEABLE_CONCRETE_POWDER))
                    .save(exporter, getRecipePath("ott", colorName + "_concrete_powder_from_dyeing"));

            // Terracotta
            Item terracotta = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(colorName + "_terracotta"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, terracotta)
                    .requires(ModTags.Items.DYEABLE_TERRACOTTA)
                    .requires(dye)
                    .unlockedBy("has_any_terracotta", has(ModTags.Items.DYEABLE_TERRACOTTA))
                    .save(exporter, getRecipePath("ott", colorName + "_terracotta_from_dyeing"));
        }
    }

    private void woodRecipes(RecipeOutput noAdv) {
        // --- Backported Pale Oak recipes (minecraft namespace) ---
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PALE_OAK_PLANKS.get(), 4)
                .requires(ModTags.Items.PALE_OAK_LOGS)
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
                .requires(ModTags.Items.woodSetLogs(setName))
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
