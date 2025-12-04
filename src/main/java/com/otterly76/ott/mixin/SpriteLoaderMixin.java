package com.otterly76.ott.mixin;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin({SpriteLoader.class})
public abstract class SpriteLoaderMixin {
    @Shadow
    @Final
    private int minHeight;
    @Shadow
    @Final
    private int minWidth;
    @Shadow
    @Final
    private ResourceLocation location;
    @Shadow
    @Final
    private int maxSupportedTextureSize;

    @Shadow
    protected abstract Map<ResourceLocation, TextureAtlasSprite> getStitchedSprites(Stitcher<SpriteContents> var1, int var2, int var3);

    @ModifyVariable(
            method = {"stitch"},
            at = @At("HEAD"),
            argsOnly = true
    )
    public List<SpriteContents> modifySpriteContents(List<SpriteContents> list) {
        List<SpriteContents> newList = new ArrayList<>(list);
        if (this.location.equals(ResourceLocation.tryParse("textures/atlas/particles.png"))) {
            System.out.println("Registering weather particles");
        }

        return newList;
    }
}