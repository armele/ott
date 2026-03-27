package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.entity.custom.TuffGolemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class TuffGolemColorLayer extends GeoRenderLayer<TuffGolemEntity> {

    public TuffGolemColorLayer(GeoRenderer<TuffGolemEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, TuffGolemEntity animatable, BakedGeoModel bakedModel,
                       RenderType renderType, MultiBufferSource bufferSource,
                       VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.isInvisible()) return;

        ResourceLocation colorTexture = ResourceLocation.fromNamespaceAndPath("ott",
                "textures/entity/tuff_golem/" + animatable.getDyeColor().getName() + ".png");
        RenderType colorLayer = RenderType.entityCutoutNoCull(colorTexture);
        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, colorLayer,
                bufferSource.getBuffer(colorLayer), partialTick, packedLight, OverlayTexture.NO_OVERLAY, -1);

        if (animatable.isGlued()) {
            ResourceLocation closedEyesTexture = ResourceLocation.fromNamespaceAndPath("ott",
                    "textures/entity/tuff_golem/closed_eyes.png");
            RenderType eyesLayer = RenderType.entityCutoutNoCull(closedEyesTexture);
            this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, eyesLayer,
                    bufferSource.getBuffer(eyesLayer), partialTick, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
    }
}
