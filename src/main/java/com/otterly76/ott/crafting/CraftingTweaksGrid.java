package com.otterly76.ott.crafting;

import net.minecraft.world.Container;

public record CraftingTweaksGrid(Container matrix, int startSlot, int gridSize, boolean rotateAllowed) {
}
