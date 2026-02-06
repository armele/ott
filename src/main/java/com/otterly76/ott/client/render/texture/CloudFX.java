package com.otterly76.ott.client.render.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteTicker;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Random;

public class CloudFX extends SpriteContents {
    final Ticker _ticker;

    protected CloudFX(ResourceLocation loc, FrameSize frameSize, NativeImage image, ResourceMetadata metadata, int alpha, float minIntensity) {
        super(loc, frameSize, image, metadata);
        this._ticker = new Ticker(this, (alpha & 255) << 24, minIntensity);
    }

    public @Nullable Ticker createTicker() {
        return this._ticker;
    }

    public void uploadFirstFrame(int x, int y) {
        NativeImage image = this._ticker._contents.getOriginalImage();

        for(int i = 0; i < 128; ++i) {
            this._ticker.generateImage(image);
        }

        this._ticker.tickAndUpload(x, y);
    }

    public static class Ticker implements SpriteTicker {
        private final Random rand = new Random();
        float[] red;
        float[] green;
        final float[] blue;
        final float[] alpha;
        final int _alpha;
        final float _minIntensity;
        private final SpriteContents _contents;

        public Ticker(SpriteContents contents, int alphaAmount, float minIntensity) {
            this._contents = contents;
            int squaredSize = this._contents.width() * this._contents.height();
            this.red = new float[squaredSize];
            this.green = new float[squaredSize];
            this.blue = new float[squaredSize];
            this.alpha = new float[squaredSize];
            this._alpha = alphaAmount;
            this._minIntensity = minIntensity;
        }

        public void tickAndUpload(int jim, int bob) {
            this.generateImage(this._contents.getOriginalImage());

            for(int mip = 0; mip < this._contents.byMipLevel.length; ++mip) {
                if (this._contents.width() >> mip > 0 && this._contents.height() >> mip > 0) {
                    NativeImage nativeImage = this._contents.byMipLevel[mip];

                    for(int x = 0; x < nativeImage.getWidth(); ++x) {
                        for(int y = 0; y < nativeImage.getHeight(); ++y) {
                            nativeImage.setPixelRGBA(x, y, this._contents.getOriginalImage().getPixelRGBA(x << mip, y << mip));
                        }
                    }

                    nativeImage.upload(mip, jim >> mip, bob >> mip, 0, 0, this._contents.width() >> mip, this._contents.height() >> mip, this._contents.byMipLevel.length > 1, false);
                }
            }

        }

        public void close() {
        }

        public void generateImage(NativeImage image) {
            int tileSizeBase = image.getWidth();
            this.evolveNoise(tileSizeBase, tileSizeBase - 1);

            for(int x = 0; x < tileSizeBase; ++x) {
                for(int y = 0; y < tileSizeBase; ++y) {
                    float v = this.red[x + tileSizeBase * y];
                    int intensity = (int)(Mth.clamp(v * 1.5F, 0.1F, 1.0F) * 255.0F) & 255;
                    int rgba = 65793 * intensity | this._alpha;
                    image.setPixelRGBA(x, y, rgba);
                }
            }

        }

        private void evolveNoise(int tileSizeBase, int tileSizeMask) {
            for(int x = 0; x < tileSizeBase; ++x) {
                for(int y = 0; y < tileSizeBase; ++y) {
                    int col = x & tileSizeMask;
                    int colAdj = x + 1 & tileSizeMask;
                    int row = (y & tileSizeMask) * tileSizeBase;
                    int rowAdj = (y + 1 & tileSizeMask) * tileSizeBase;
                    int pixelIndex = row + col;
                    float redModifier = -0.0215F;

                    for(int k = x - 1; k <= x + 1; ++k) {
                        for(int l = y - 1; l <= y + 1; ++l) {
                            redModifier += this.red[(k & tileSizeMask) + (l & tileSizeMask) * tileSizeBase];
                        }
                    }

                    float blueAverage = (this.blue[pixelIndex] + this.blue[colAdj + row] + this.blue[colAdj + rowAdj] + this.blue[col + rowAdj]) * 0.25F;
                    this.green[pixelIndex] = redModifier * 0.1005F + blueAverage;
                    float[] var10000 = this.blue;
                    var10000[pixelIndex] += this.alpha[pixelIndex] * 0.0108F;
                    float amount = 0.0425F;
                    float factor = 0.1F;
                    var10000 = this.alpha;
                    var10000[pixelIndex] -= amount;
                    if (this.blue[pixelIndex] < 0.0F) {
                        this.blue[pixelIndex] = 0.0F;
                    }

                    if (!(this.rand.nextFloat() >= this._minIntensity)) {
                        this.alpha[pixelIndex] = 1.0F;
                    }
                }
            }

            float[] greenTemp = this.green;
            this.green = this.red;
            this.red = greenTemp;
        }
    }
}
