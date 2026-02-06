package com.otterly76.ott.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record FoodTooltipComponent(int hunger, float saturation) implements TooltipComponent {}
