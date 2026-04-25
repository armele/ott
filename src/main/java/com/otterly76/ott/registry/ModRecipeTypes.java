package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import com.otterly76.ott.recipe.EngravingRecipe;
import com.otterly76.ott.recipe.WoodcuttingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<WoodcuttingRecipe>> WOODCUTTING =
            RECIPE_TYPES.register("woodcutting", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "woodcutting")));

    public static final DeferredHolder<RecipeType<?>, RecipeType<EngravingRecipe>> ENGRAVING =
            RECIPE_TYPES.register("engraving", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "engraving")));

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}
