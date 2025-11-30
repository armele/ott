package com.otterly76.ott.generation;

import com.otterly76.ott.block.IGradientBlock;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class GradientBlockRecipeProvider extends RecipeProvider {
    public GradientBlockRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ModBlocks.ALL_GRADIENT_BLOCKS.forEach(deferredBlock -> {
            createGradientRecipe(recipeOutput, deferredBlock.get());
        });
    }

    private void createGradientRecipe(RecipeOutput recipeOutput, IGradientBlock gradientBlock) {
        Block block = (Block) gradientBlock;
        Block ingredient1 = gradientBlock.getBlockFromColor(gradientBlock.getFirstColor());
        Block ingredient2 = gradientBlock.getBlockFromColor(gradientBlock.getSecondColor());

        // Create a shapeless recipe: Color 1 + Color 2 -> 2 Gradient Blocks
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, block, 2)
                .requires(ingredient1)
                .requires(ingredient2)
                .group("ott_gradient_blocks")
                .unlockedBy("has_" + getItemName(ingredient1), has(ingredient1))
                .unlockedBy("has_" + getItemName(ingredient2), has(ingredient2))
                .save(recipeOutput);
    }
}