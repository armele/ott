package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.WendigoModel;
import com.otterly76.ott.entity.custom.WendigoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class WendigoRenderer extends GeoEntityRenderer<WendigoEntity> {
    public WendigoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WendigoModel());
    }

    @Override
    public void preRender(PoseStack poseStack, WendigoEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isRebind, float partialTick, int packedLight, int packedOverlay, int color) {
        float scale = 1.0F + (animatable.getKillCount() * 0.1F);
        poseStack.scale(scale, scale, scale);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isRebind, partialTick, packedLight, packedOverlay, color);
    }
}