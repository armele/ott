package com.otterly76.ott.util.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FoodUtil {
    public record FoodValues(int hunger, float saturation) {}

    @Nullable
    public static FoodValues getFoodValues(ItemStack stack, Player player) {
        FoodProperties foodProperties = stack.getFoodProperties(player);
        if (foodProperties == null) {
            return null;
        }
        return new FoodValues(foodProperties.nutrition(), foodProperties.saturation());
    }
}
