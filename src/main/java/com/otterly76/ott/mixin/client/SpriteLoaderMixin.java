package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.otterly76.ott.ClientModEvents;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.otterly76.ott.Constants.MOD_ID;

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
        if (this.location.equals(ResourceLocation.withDefaultNamespace("textures/atlas/particles.png"))) {
            ClientModEvents.particleCount = 0;
            ClientModEvents.fogCount = 0;
            NativeImage rainImage;
            NativeImage snowImage;

            try {
                rainImage = ClientModEvents.loadTexture(ResourceLocation.withDefaultNamespace("textures/environment/rain.png"));
                snowImage = ClientModEvents.loadTexture(ResourceLocation.withDefaultNamespace("textures/environment/snow.png"));
                if (OttConfig.WEATHER.BIOME_TINT.get()) {
                    rainImage.applyToAllPixels(ClientModEvents.desaturateOperation);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to load OTT environment textures", e);
            }

            for(int j = 0; j < 4; ++j) {
                newList.add(ClientModEvents.splitImage(rainImage, j, "rain"));
            }

            for(int j = 0; j < 4; ++j) {
                newList.add(ClientModEvents.splitImage(snowImage, j, "snow"));
            }

            int rippleResolution = ClientModEvents.getRippleResolution(newList);

            for(int j = 0; j < 8; ++j) {
                newList.add(ClientModEvents.generateRipple(j, rippleResolution));
            }

            if (OttConfig.WEATHER.BIOME_TINT.get()) {
                for(int j = 0; j < 4; ++j) {
                    NativeImage splashImage;

                    try {
                        splashImage = ClientModEvents.loadTexture(ResourceLocation.withDefaultNamespace("textures/particle/splash_" + j + ".png"));
                        splashImage.applyToAllPixels(ClientModEvents.desaturateOperation);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to load OTT splash particles", e);
                    }

                    newList.add(new SpriteContents(ResourceLocation.fromNamespaceAndPath(MOD_ID, "splash" + j), new FrameSize(splashImage.getWidth(), splashImage.getHeight()), splashImage, ResourceMetadata.EMPTY));
                }
            }
        }

        return newList;
    }
}