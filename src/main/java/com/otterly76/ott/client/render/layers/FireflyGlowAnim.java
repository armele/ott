package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FireflyGlowAnim implements VertexConsumer {
    private static final int FRAME_COUNT = 30;

    private final VertexConsumer vertexConsumer;
    private final int frameIndex;

    public FireflyGlowAnim(VertexConsumer vertexConsumer, int frameIndex) {
        this.vertexConsumer = vertexConsumer;
        this.frameIndex = Math.max(0, Math.min(frameIndex, FRAME_COUNT - 1));
    }

    @Override
    public @NotNull VertexConsumer addVertex(float x, float y, float z) {
        vertexConsumer.addVertex(x, y, z);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setColor(int r, int g, int b, int a) {
        vertexConsumer.setColor(r, g, b, a);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float u, float v) {
        float vScaled = (v + (float)frameIndex) / (float)FRAME_COUNT;
        vertexConsumer.setUv(u, vScaled);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv1(int u, int v) {
        vertexConsumer.setUv1(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv2(int u, int v) {
        vertexConsumer.setUv2(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setNormal(float x, float y, float z) {
        vertexConsumer.setNormal(x, y, z);
        return this;
    }
}