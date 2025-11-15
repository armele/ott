package com.otterly76.ott.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

public interface IGradientBlock {
    DyeColor getFirstColor();

    DyeColor getSecondColor();

    String getTextureName(DyeColor dyeColor);

    ResourceLocation getRegistryID();

    default ResourceLocation getRenderType() {
        return ResourceLocation.withDefaultNamespace(RenderType.solid().name);
    }

    Block getBlockFromColor(final DyeColor color);
}
