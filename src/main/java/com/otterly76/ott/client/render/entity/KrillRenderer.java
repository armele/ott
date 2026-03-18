package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.KrillModel;
import com.otterly76.ott.entity.custom.Krill;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class KrillRenderer extends GeoEntityRenderer<Krill> {
    private static final ResourceLocation DEFAULT = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/krill/krill.png");
    private static final ResourceLocation DEFAULT_LAYER = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/krill/krill_layer.png");

    public KrillRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new KrillModel());
        this.addRenderLayer(new KrillTranslucentLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Krill entity) {
        return DEFAULT;
    }

    public static class KrillTranslucentLayer extends GeoRenderLayer<Krill> {
        public KrillTranslucentLayer(GeoRenderer<Krill> renderer) {
            super(renderer);
        }

        protected RenderType getRenderType(Krill animatable) {
            return RenderType.entityTranslucent(DEFAULT_LAYER);
        }

        @Override
        public void render(PoseStack poseStack, Krill animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            renderType = this.getRenderType(animatable);
            this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, bufferSource.getBuffer(renderType), true, partialTick, 15728640, OverlayTexture.NO_OVERLAY, -1);
        }
    }
}