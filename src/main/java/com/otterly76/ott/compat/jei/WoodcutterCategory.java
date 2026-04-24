package com.otterly76.ott.compat.jei;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.recipe.WoodcuttingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class WoodcutterCategory extends AbstractRecipeCategory<WoodcuttingRecipe> {

    public static final RecipeType<WoodcuttingRecipe> RECIPE_TYPE = new RecipeType<>(
            ResourceLocation.fromNamespaceAndPath("ott", "woodcutting"),
            WoodcuttingRecipe.class
    );

    public WoodcutterCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("container.woodcutter"),
                guiHelper.createDrawableItemLike(ModBlocks.WOODCUTTER.get()),
                82,
                26
        );
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder,
                          @NotNull WoodcuttingRecipe recipe,
                          @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .setStandardSlotBackground()
                .addItemStacks(Arrays.asList(recipe.getIngredients().getFirst().getItems()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 1)
                .setOutputSlotBackground()
                .addItemStack(recipe.getResultItem(
                        Objects.requireNonNull(Minecraft.getInstance().level).registryAccess()));
    }
}