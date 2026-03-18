package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.BarreleyeModel;
import com.otterly76.ott.entity.custom.Barreleye;
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

public class BarreleyeRenderer extends GeoEntityRenderer<Barreleye> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/barreleye/barreleye_fish.png");
    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/barreleye/barreleye_fish_layer.png");

    public BarreleyeRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BarreleyeModel());
        this.addRenderLayer(new BarreleyeTranslucentLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Barreleye entity) {
        return TEXTURE;
    }

    public static class BarreleyeTranslucentLayer extends GeoRenderLayer<Barreleye> {
        public BarreleyeTranslucentLayer(GeoRenderer<Barreleye> renderer) {
            super(renderer);
        }

        protected RenderType getRenderType(Barreleye animatable) {
            return RenderType.entityTranslucent(LAYER);
        }

        @Override
        public void render(PoseStack poseStack, Barreleye animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            renderType = this.getRenderType(animatable);
            this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, bufferSource.getBuffer(renderType), true, partialTick, 15728640, OverlayTexture.NO_OVERLAY, -1);
        }
    }
}