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
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
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
}