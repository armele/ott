package com.otterly76.ott.mixin.access;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    @Invoker("blitNineSlicedSprite")
    void callBlitNineSlicedSprite(TextureAtlasSprite sprite, GuiSpriteScaling.NineSlice scaling, int x, int y, int width, int height, int color);
}
