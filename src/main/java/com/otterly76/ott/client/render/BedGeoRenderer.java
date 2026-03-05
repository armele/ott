package com.otterly76.ott.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.BedAnimatable;
import com.otterly76.ott.client.model.BedGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;

public class BedGeoRenderer implements GeoRenderer<BedAnimatable> {
    private final BedGeoModel model = new BedGeoModel();
    private final BedAnimatable animatable = new BedAnimatable();

    @Override
    public BedGeoModel getGeoModel() { return this.model; }

    @Override
    public BedAnimatable getAnimatable() { return this.animatable; }

    @Override
    public ResourceLocation getTextureLocation(BedAnimatable animatable) {
        return this.model.getTextureResource(animatable);
    }

    @Override
    public RenderType getRenderType(BedAnimatable animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutout(texture);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, BedAnimatable animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
    }

    @Override public List<GeoRenderLayer<BedAnimatable>> getRenderLayers() { return List.of(); }

    @Override public void fireCompileRenderLayersEvent() {}

    @Override public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) { return true; }

    @Override public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {}

    @Override public void updateAnimatedTextureFrame(BedAnimatable animatable) {}
}