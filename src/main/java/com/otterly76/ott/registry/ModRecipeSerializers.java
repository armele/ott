package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import com.otterly76.ott.recipe.BundleColoring;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Constants.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> MINECRAFT_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, "minecraft");

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<BundleColoring>> BUNDLE_COLORING =
            MINECRAFT_SERIALIZERS.register("crafting_special_bundlecoloring", () -> new SimpleCraftingRecipeSerializer<>(BundleColoring::new));

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        MINECRAFT_SERIALIZERS.register(eventBus);
    }
}
