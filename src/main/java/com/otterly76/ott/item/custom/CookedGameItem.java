package com.otterly76.ott.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CookedGameItem extends Item {
    public CookedGameItem() {
        super(new Item.Properties()
                .stacksTo(64)
                .food(new FoodProperties.Builder()
                        .nutrition(7)
                        .saturationModifier(1.6F)
                        .build()));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, world, entity);

        if (stack.isEmpty()) {
            return new ItemStack(Items.BOWL);
        } else {
            if (entity instanceof Player player && !player.getAbilities().instabuild) {
                ItemStack bowl = new ItemStack(Items.BOWL);
                if (!player.getInventory().add(bowl)) {
                    player.drop(bowl, false);
                }
            }
            return result;
        }
    }
}