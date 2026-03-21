package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.model.CapybaraModel;
import com.otterly76.ott.entity.custom.CapybaraEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CapybaraRenderer extends GeoEntityRenderer<CapybaraEntity> {
    public CapybaraRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CapybaraModel());
    }

    @Override
    public void preRender(PoseStack poseStack, CapybaraEntity animatable, software.bernie.geckolib.cache.object.BakedGeoModel model, MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer, boolean isReRerender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRerender, partialTick, packedLight, packedOverlay, colour);
    }
}