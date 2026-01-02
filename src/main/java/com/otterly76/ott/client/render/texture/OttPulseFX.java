package com.otterly76.ott.client.render.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteTicker;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import java.awt.Color;

public class OttPulseFX extends SpriteContents {
    private final Ticker ticker;

    public OttPulseFX(ResourceLocation loc, FrameSize frameSize, NativeImage image, ResourceMetadata metadata, float timeScale, float saturation, float hueMin, float hueMax) {
        super(loc, frameSize, image, metadata);
        this.ticker = new Ticker(this, timeScale, saturation, hueMin, hueMax);
    }

    @Override
    public @Nullable SpriteTicker createTicker() { return this.ticker; }

    public static class Ticker implements SpriteTicker {
        private final SpriteContents contents;
        private final float timeScale;
        private final float saturation;
        private final float hueMin;
        private final float hueMax;

        public Ticker(SpriteContents contents, float timeScale, float saturation, float hueMin, float hueMax) {
            this.contents = contents;
            this.timeScale = timeScale;
            this.saturation = saturation;
            this.hueMin = hueMin;
            this.hueMax = hueMax;
        }

        @Override
        public void tickAndUpload(int xOffset, int yOffset) {
            NativeImage image = this.contents.getOriginalImage();
            int width = this.contents.width();
            int height = this.contents.height();

            // 1. Time-based math
            float systemTime = (System.nanoTime() / 1_000_000_000.0f);

            // 2. Pulse brightness (0.7 to 1.0)
            float pulse = (float) (Math.sin(systemTime * (6.28f / timeScale)) * 0.15 + 0.85);

            // 3. Shifting Hue (Ping-Pong style)
            float hueProgress = (float) (Math.sin(systemTime * (3.14f / timeScale)) * 0.5 + 0.5);
            float hue = Mth.lerp(hueProgress, hueMin, hueMax);

            // 4. Calculate the final ABGR color for this frame
            int rgb = Color.HSBtoRGB(hue, saturation, pulse);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;
            int abgr = (0xFF << 24) | (b << 16) | (g << 8) | r;

            // Fill the texture with the solid pulsing color
            for (int i = 0; i < width * height; i++) {
                image.setPixelRGBA(i % width, i / width, abgr);
            }

            image.upload(0, xOffset, yOffset, 0, 0, width, height, false, false);
        }

        @Override public void close() {}
    }
}