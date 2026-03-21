package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.Seahorse1Model;
import com.otterly76.ott.entity.custom.Seahorse1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Seahorse1Renderer extends GeoEntityRenderer<Seahorse1Entity> {
    public Seahorse1Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Seahorse1Model());
    }

    @Override
    public void preRender(PoseStack poseStack, Seahorse1Entity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRerender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRerender, partialTick, packedLight, packedOverlay, 0xFF000000 | animatable.getColor());
    }
}
