package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.SmallFireflyModel;
import com.otterly76.ott.client.render.layers.SmallFireflyGlowLayer;
import com.otterly76.ott.entity.custom.SmallFirefly;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SmallFireflyRenderer extends GeoEntityRenderer<SmallFirefly> {
    public SmallFireflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SmallFireflyModel());
        this.addRenderLayer(new SmallFireflyGlowLayer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, SmallFirefly animatable, software.bernie.geckolib.cache.object.BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.scale(0.4F, 0.4F, 0.4F);
    }
}