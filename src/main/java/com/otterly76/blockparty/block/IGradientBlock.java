package com.otterly76.blockparty.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public interface IGradientBlock {
    DyeColor getFirstColor();

    DyeColor getSecondColor();

    String getTextureName(DyeColor dyeColor);

    ResourceLocation getRegistryID();

    default ResourceLocation getRenderType() {
        return ResourceLocation.withDefaultNamespace(RenderType.solid().name);
    }
}
