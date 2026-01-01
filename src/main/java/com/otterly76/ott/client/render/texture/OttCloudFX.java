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

    public OttCloudFX(ResourceLocation loc, FrameSize frameSize, NativeImage image, ResourceMetadata metadata, int alpha, float minIntensity) {
        super(loc, frameSize, image, metadata);
        this.ticker = new Ticker(this, alpha, minIntensity);
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

        private float[] bufferA;
        private float[] bufferB;
        private final float[] heat;
        private final float[] spark;

        public Ticker(SpriteContents contents, int alpha, float minIntensity) {
            this.contents = contents;
            this.alphaValue = (alpha & 0xFF) << 24;
            this.minIntensity = minIntensity;

            int size = contents.width() * contents.height();
            this.bufferA = new float[size];
            this.bufferB = new float[size];
            this.heat = new float[size];
            this.spark = new float[size];
        }

        @Override
        public void tickAndUpload(int xOffset, int yOffset) {
            this.evolve();
            NativeImage image = this.contents.getOriginalImage();
            int width = this.contents.width();
            int height = this.contents.height();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    float v = this.bufferA[x + y * width];
                    int intensity = (int)(Mth.clamp(v * 1.5F, 0.1F, 1.0F) * 255.0F) & 0xFF;
                    // 0x010101 * intensity creates grayscale R=G=B
                    int rgba = (intensity << 16) | (intensity << 8) | intensity | this.alphaValue;
                    image.setPixelRGBA(x, y, rgba);
                }
            }

            // FIX: Instead of calling the protected SpriteContents.upload,
            // we upload the base mip level (0) directly from the NativeImage.
            image.upload(0, xOffset, yOffset, 0, 0, width, height, false, false);
        }

        private void evolve() {
            int w = this.contents.width();
            int h = this.contents.height();
            int maskX = w - 1;
            int maskY = h - 1;

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    float neighbors = 0.0F;
                    // Sum up the surrounding "energy"
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            neighbors += this.bufferA[((x + dx) & maskX) + ((y + dy) & maskY) * w];
                        }
                    }

                    int idx = x + y * w;
                    float avgHeat = (this.heat[idx] +
                            this.heat[(x + 1 & maskX) + y * w] +
                            this.heat[x + (y + 1 & maskY) * w]) * 0.33F;

                    this.bufferB[idx] = (neighbors - 0.02F) * 0.1F + avgHeat;
                    this.heat[idx] += this.spark[idx] * 0.01F;
                    this.spark[idx] -= 0.04F;

                    if (this.heat[idx] < 0.0F) this.heat[idx] = 0.0F;
                    if (this.random.nextFloat() < this.minIntensity) this.spark[idx] = 1.0F;
                }
            }

            // Swap buffers for the next frame
            float[] temp = this.bufferA;
            this.bufferA = this.bufferB;
            this.bufferB = temp;
        }

        @Override
        public void close() {}
    }
}