package com.otterly76.ott.generation;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class DynamicRecipeProvider extends AbstractRecipeProvider {
    public DynamicRecipeProvider(DataProviderContext context) {
        super(context);
    }

    public void addRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.NAME_TAG).define('#', Items.STRING).define('X', Items.PAPER).pattern("  #").pattern(" X ").pattern("X  ").unlockedBy(getHasName(Items.PAPER), has(Items.PAPER)).save(recipeOutput);
    }
}