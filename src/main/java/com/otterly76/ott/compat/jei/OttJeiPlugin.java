package com.otterly76.ott.compat.jei;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.recipe.WoodcuttingRecipe;
import com.otterly76.ott.registry.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class OttJeiPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("ott", "jei");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WoodcutterCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<WoodcuttingRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.WOODCUTTING.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(WoodcutterCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WOODCUTTER.get()), WoodcutterCategory.RECIPE_TYPE);
    }
}
