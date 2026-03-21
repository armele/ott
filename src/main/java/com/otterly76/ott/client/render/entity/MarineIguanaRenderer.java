package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.MarineIguanaModel;
import com.otterly76.ott.entity.custom.MarineIguana;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class MarineIguanaRenderer extends GeoEntityRenderer<MarineIguana> {
    public MarineIguanaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MarineIguanaModel());
        this.addRenderLayer(new IguanaGlowingLayer(this));
    }

    @Override
    protected float getShadowRadius(@NotNull MarineIguana entity) {
        return 0.4F;
    }

    public static class IguanaGlowingLayer extends GeoRenderLayer<MarineIguana> {
        public IguanaGlowingLayer(GeoEntityRenderer<MarineIguana> renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MarineIguana animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (animatable.getVariant() == 1 || animatable.isGojira()) {
                RenderType glowRenderType = RenderType.eyes(getTextureResource(animatable));
                getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1);
            }
        }
    }
}
