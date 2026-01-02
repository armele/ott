package com.otterly76.ott.client.render.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteTicker;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class OttHeartbeatFX extends SpriteContents {
    private final Ticker ticker;

    public OttHeartbeatFX(ResourceLocation loc, FrameSize frameSize, NativeImage image, ResourceMetadata metadata, float timeScale, float minBrightness) {
        super(loc, frameSize, image, metadata);
        this.ticker = new Ticker(this, timeScale, minBrightness);
    }

    @Override
    public @Nullable SpriteTicker createTicker() { return this.ticker; }

    public static class Ticker implements SpriteTicker {
        private final SpriteContents contents;
        private final float timeScale;
        private final float minBrightness;

        public Ticker(SpriteContents contents, float timeScale, float minBrightness) {
            this.contents = contents;
            this.timeScale = timeScale;
            this.minBrightness = minBrightness;
        }

        @Override
        public void tickAndUpload(int xOffset, int yOffset) {
            NativeImage image = this.contents.getOriginalImage();
            int width = this.contents.width();
            int height = this.contents.height();

            float time = (System.nanoTime() / 1_000_000_000.0f) / timeScale;
            float spatialScale = 32.0f; // Scale of 1.0 per block

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    float wave = (float) Math.sin((time * 6.28f) + (x / spatialScale) + (y / spatialScale));
                    float pulse = wave * 0.5f + 0.5f;

                    int b = (int) (Mth.lerp(pulse, minBrightness * 255, 255));
                    int abgr = (0xFF << 24) | (b << 16) | (b << 8) | b;
                    image.setPixelRGBA(x, y, abgr);
                }
            }
            image.upload(0, xOffset, yOffset, 0, 0, width, height, false, false);
        }

        @Override public void close() {}
    }
}