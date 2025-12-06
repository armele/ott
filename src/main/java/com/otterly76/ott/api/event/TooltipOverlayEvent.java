package com.otterly76.ott.api.event;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class TooltipOverlayEvent extends Event implements ICancellableEvent {
    public FoodProperties defaultFood;
    public FoodProperties modifiedFood;
    public ItemStack itemStack;

    protected TooltipOverlayEvent(ItemStack itemStack, FoodProperties defaultFood, FoodProperties modifiedFood) {
        this.itemStack = itemStack;
        this.defaultFood = defaultFood;
        this.modifiedFood = modifiedFood;
    }

    public static class Pre extends TooltipOverlayEvent {
        public Pre(ItemStack itemStack, FoodProperties defaultFood, FoodProperties modifiedFood) {
            super(itemStack, defaultFood, modifiedFood);
        }
    }
}