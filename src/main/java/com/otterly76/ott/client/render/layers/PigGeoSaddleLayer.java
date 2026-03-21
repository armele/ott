package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.PigGeoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class PigGeoSaddleLayer<T extends Pig & PigGeoEntity> extends GeoRenderLayer<T> {
    private static final ResourceLocation SADDLE_TEXTURE = Ott.resource("textures/entity/pig/pig_saddle.png");

    public PigGeoSaddleLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.isSaddled()) {
            RenderType saddleRenderType = RenderType.entityCutoutNoCull(SADDLE_TEXTURE);
            getRenderer().actuallyRender(poseStack, animatable, model, saddleRenderType, bufferSource, bufferSource.getBuffer(saddleRenderType), false, partialTick, packedLight, packedOverlay, -1);
        }
    }
}
