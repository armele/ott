package com.otterly76.ott.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class FoodTooltip implements TooltipComponent {
    private final ItemStack itemStack;
    private final FoodProperties defaultFood;
    private final FoodProperties modifiedFood;
    private final Player player;

    public FoodTooltip(ItemStack itemStack, FoodProperties defaultFood, FoodProperties modifiedFood, Player player) {
        this.itemStack = itemStack;
        this.defaultFood = defaultFood;
        this.modifiedFood = modifiedFood;
        this.player = player;
    }

    public boolean shouldRenderHungerBars() {
        return modifiedFood != null && modifiedFood.nutrition() > 0;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public FoodProperties getDefaultFood() {
        return defaultFood;
    }

    public FoodProperties getModifiedFood() {
        return modifiedFood;
    }

    public Player getPlayer() {
        return player;
    }
}