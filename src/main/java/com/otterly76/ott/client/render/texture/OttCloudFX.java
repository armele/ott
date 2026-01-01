package com.otterly76.ott.client.render.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteTicker;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class OttCloudFX extends SpriteContents {
    private final Ticker ticker;

    public OttCloudFX(ResourceLocation loc, FrameSize frameSize, NativeImage image, ResourceMetadata metadata, int alpha, float minIntensity, @Nullable Integer c1, @Nullable Integer c2) {
        super(loc, frameSize, image, metadata);
        this.ticker = new Ticker(this, alpha, minIntensity, c1, c2);
    }

    @Override
    public @Nullable SpriteTicker createTicker() {
        return this.ticker;
    }

    public static class Ticker implements SpriteTicker {
        private final Random random = new Random();
        private final SpriteContents contents;
        private final int alphaValue;
        private final float minIntensity;

        // Two independent buffers for two "cloud" layers
        private float[] layerA;
        private float[] layerB;
        private float[] tempA;
        private float[] tempB;

        private final float[] heat;
        private final float[] spark;

        @Nullable private final Integer color1;
        @Nullable private final Integer color2;

        public Ticker(SpriteContents contents, int alpha, float minIntensity, @Nullable Integer c1, @Nullable Integer c2) {
            this.contents = contents;
            this.alphaValue = (alpha & 0xFF) << 24;
            this.minIntensity = minIntensity;

            int size = contents.width() * contents.height();
            this.layerA = new float[size];
            this.layerB = new float[size];
            this.tempA = new float[size];
            this.tempB = new float[size];
            this.heat = new float[size];
            this.spark = new float[size];
            this.color1 = c1;
            this.color2 = c2;

        }

        @Override
        public void tickAndUpload(int xOffset, int yOffset) {
            this.evolve();
            NativeImage image = this.contents.getOriginalImage();
            int width = this.contents.width();
            int height = this.contents.height();

            if (color1 != null && color2 != null) {
                renderNebula(image, width, height);
            } else {
                renderGrayscale(image, width, height);
            }

            image.upload(0, xOffset, yOffset, 0, 0, width, height, false, false);
        }

        private void renderGrayscale(NativeImage image, int width, int height) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    float v = Mth.clamp(this.layerA[x + y * width] * 1.5F, 0.1F, 1.0F);
                    int intensity = (int)(v * 255.0F) & 0xFF;
                    int rgba = (intensity << 16) | (intensity << 8) | intensity | this.alphaValue;
                    image.setPixelRGBA(x, y, rgba);
                }
            }
        }

        private void renderNebula(NativeImage image, int width, int height) {
            // Explicit null check to silence the "unboxing" warning
            if (color1 == null || color2 == null) return;

            int c1 = color1;
            int c2 = color2;
            int r1 = (c1 >> 16) & 255; int g1 = (c1 >> 8) & 255; int b1 = c1 & 255;
            int r2 = (c2 >> 16) & 255; int g2 = (c2 >> 8) & 255; int b2 = c2 & 255;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int idx = x + y * width;
                    float v1 = Mth.clamp(this.layerA[idx] * 1.5F, 0.0F, 1.0F);
                    float v2 = Mth.clamp(this.layerB[idx] * 1.5F, 0.0F, 1.0F);

                    int r = (int) Mth.lerp(v1, r1, r2);
                    int g = (int) Mth.lerp(v2, g1, g2);
                    int b = (int) Mth.lerp(v1, b1, b2);

                    // ABGR format: (Alpha << 24) | (Blue << 16) | (Green << 8) | Red
                    int rgba = (b << 16) | (g << 8) | r | this.alphaValue;
                    image.setPixelRGBA(x, y, rgba);
                }
            }
        }

        private void evolve() {
            int w = this.contents.width();
            int h = this.contents.height();
            int maskX = w - 1;
            int maskY = h - 1;

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    float neighborsA = 0.0F;
                    float neighborsB = 0.0F;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            neighborsA += this.layerA[((x + dx) & maskX) + ((y + dy) & maskY) * w];
                            // Slightly different sampling for layer B to make it move differently
                            neighborsB += this.layerB[((x + dy) & maskX) + ((y + dx) & maskY) * w];
                        }
                    }

                    int idx = x + y * w;
                    float avgHeat = (this.heat[idx] + this.heat[(x + 1 & maskX) + y * w] + this.heat[x + (y + 1 & maskY) * w]) * 0.33F;

                    this.tempA[idx] = (neighborsA - 0.02F) * 0.1F + avgHeat;
                    this.tempB[idx] = (neighborsB - 0.02F) * 0.101F + (avgHeat * 0.9F); // Slightly slower

                    this.heat[idx] += this.spark[idx] * 0.01F;
                    this.spark[idx] -= 0.04F;

                    if (this.heat[idx] < 0.0F) this.heat[idx] = 0.0F;
                    if (this.random.nextFloat() < this.minIntensity) this.spark[idx] = 1.0F;
                }
            }

            // Swap buffers
            float[] tA = this.layerA; this.layerA = this.tempA; this.tempA = tA;
            float[] tB = this.layerB; this.layerB = this.tempB; this.tempB = tB;
        }

        @Override
        public void close() {}
    }
}