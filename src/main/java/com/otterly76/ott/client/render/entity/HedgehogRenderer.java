package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.HedgehogModel;
import com.otterly76.ott.entity.custom.HedgehogEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HedgehogRenderer extends GeoEntityRenderer<HedgehogEntity> {
    public HedgehogRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HedgehogModel());
    }

    @Override
    public void preRender(PoseStack poseStack, HedgehogEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRerender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRerender, partialTick, packedLight, packedOverlay, colour);
    }
}
