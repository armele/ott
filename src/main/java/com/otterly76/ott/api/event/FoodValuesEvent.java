package com.otterly76.ott.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public class FoodValuesEvent extends Event {
    public FoodProperties defaultFoodProperties;
    public FoodProperties modifiedFoodProperties;
    public final ItemStack itemStack;
    public final Player player;

    public FoodValuesEvent(Player player, ItemStack itemStack, FoodProperties defaultFoodProperties, FoodProperties modifiedFoodProperties) {
        this.player = player;
        this.itemStack = itemStack;
        this.defaultFoodProperties = defaultFoodProperties;
        this.modifiedFoodProperties = modifiedFoodProperties;
    }
}