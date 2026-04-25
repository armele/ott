package com.otterly76.ott.recipe;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.registry.ModRecipeSerializers;
import com.otterly76.ott.registry.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EngravingRecipe extends SingleItemRecipe {

    public EngravingRecipe(String group, Ingredient ingredient, ItemStack result) {
        super(ModRecipeTypes.ENGRAVING.get(), ModRecipeSerializers.ENGRAVING.get(), group, ingredient, result);
    }

    @Override
    public boolean matches(@NotNull SingleRecipeInput input, @NotNull Level level) {
        return this.ingredient.test(input.item());
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.ENGRAVING_TABLE.get());
    }

    public static class Serializer extends SingleItemRecipe.Serializer<EngravingRecipe> {
        public Serializer() {
            super(EngravingRecipe::new);
        }
    }
}
