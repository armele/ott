package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.SeahorseModel;
import com.otterly76.ott.entity.custom.SeahorseEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SeahorseRenderer extends GeoEntityRenderer<SeahorseEntity> {
    public SeahorseRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SeahorseModel());
    }

    @Override
    public void preRender(PoseStack poseStack, SeahorseEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRerender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRerender, partialTick, packedLight, packedOverlay, 0xFF000000 | animatable.getColor());
    }
}
