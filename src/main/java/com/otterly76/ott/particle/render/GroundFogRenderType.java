package com.otterly76.ott.particle.render;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GroundFogRenderType implements ParticleRenderType {
    public static final ParticleRenderType INSTANCE = new GroundFogRenderType();

    public BufferBuilder begin(Tesselator tesselator, @NotNull TextureManager textureManager) {
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderTexture(0, ResourceLocation.withDefaultNamespace("textures/atlas/particles.png"));
        RenderSystem.setShaderTexture(2, Minecraft.getInstance().gameRenderer.lightTexture().lightTextureLocation);
        return tesselator.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    public String toString() {
        return "ott:ground_fog";
    }

}