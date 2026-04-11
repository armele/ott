package com.otterly76.ott.mixin.core;

import com.otterly76.ott.polymorph.CraftingRecipeManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class PolymorphCraftingMenuMixin {

    @Redirect(
            at = @At(value = "INVOKE",
                    target = "net/minecraft/world/item/crafting/RecipeManager.getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/RecipeHolder;)Ljava/util/Optional;"),
            method = "slotChangedCraftingGrid")
    private static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> ott$getRecipe(
            RecipeManager recipeManager, RecipeType<T> type, I craftingInput, Level world,
            RecipeHolder<CraftingRecipe> recipeHolder,
            AbstractContainerMenu menu, Level unused, Player player,
            CraftingContainer craftingContainer, ResultContainer resultContainer,
            RecipeHolder<CraftingRecipe> unused1) {
        return CraftingRecipeManager.resolveRecipe(menu, type, craftingInput, world, player);
    }
}
