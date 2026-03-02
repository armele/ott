package com.otterly76.ott.util.color;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class ColorUtils {
    public static int scaleRGB(int color, float red, float green, float blue) {
        return ARGB32.color(ARGB32.alpha(color), Mth.clamp((int)((float)ARGB32.red(color) * red), 0, 255), Mth.clamp((int)((float)ARGB32.green(color) * green), 0, 255), Mth.clamp((int)((float)ARGB32.blue(color) * blue), 0, 255));
    }

    public static int colorFromFloat(float alpha, float red, float green, float blue) {
        return ARGB32.color((int)(alpha * 255.0F), (int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F));
    }

    public static DyeColor getMixedColor(ServerLevel level, DyeColor colorA, DyeColor colorB) {
        CraftingInput container = makeCraftColorInput(colorA, colorB);
        return level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, level)
                .map((recipe) -> recipe.value().assemble(container, level.registryAccess()))
                .map(ItemStack::getItem)
                .filter(DyeItem.class::isInstance)
                .map(DyeItem.class::cast)
                .map(DyeItem::getDyeColor)
                .orElseGet(() -> level.random.nextBoolean() ? colorA : colorB);
    }

    private static CraftingInput makeCraftColorInput(DyeColor colorA, DyeColor colorB) {
        return CraftingInput.of(2, 1, List.of(new ItemStack(DyeItem.byColor(colorA)), new ItemStack(DyeItem.byColor(colorB))));
    }
}
