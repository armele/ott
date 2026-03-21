package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.DuckGeoModel;
import com.otterly76.ott.entity.custom.Duck;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DuckGeoRenderer extends GeoLivingRendererWrapper<Duck> {
    public DuckGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new DuckGeoModel()) {
            @Override
            public void preRender(PoseStack poseStack, Duck animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
                if (!isReRender && animatable.isBaby()) {
                    poseStack.scale(0.5F, 0.5F, 0.5F);
                }
                super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
            }
        });
    }
}
