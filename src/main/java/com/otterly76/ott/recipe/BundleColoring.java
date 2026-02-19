package com.otterly76.ott.recipe;

import com.otterly76.ott.registry.ModRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class BundleColoring extends CustomRecipe {
    public BundleColoring(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int bundles = 0;
        int dyes = 0;

        for (int slot = 0; slot < input.size(); ++slot) {
            ItemStack stack = input.getItem(slot);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BundleItem) {
                    ++bundles;
                } else if (stack.getItem() instanceof DyeItem) {
                    ++dyes;
                } else {
                    return false;
                }

                if (dyes > 1 || bundles > 1) {
                    return false;
                }
            }
        }

        return bundles == 1 && dyes == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack bundle = ItemStack.EMPTY;
        DyeItem dye = (DyeItem) Items.WHITE_DYE;

        for (int slot = 0; slot < input.size(); ++slot) {
            ItemStack stack = input.getItem(slot);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (item instanceof BundleItem) {
                    bundle = stack;
                } else if (item instanceof DyeItem) {
                    dye = (DyeItem) item;
                }
            }
        }

        String colorName = dye.getDyeColor().getName();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("minecraft", colorName + "_bundle");
        Item result = BuiltInRegistries.ITEM.get(id);
        if (result == Items.AIR) {
            // Fallback to vanilla bundle if somehow the colored bundle isn't present
            result = Items.BUNDLE;
        }
        return bundle.transmuteCopy(result, 1);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.BUNDLE_COLORING.get();
    }
}
