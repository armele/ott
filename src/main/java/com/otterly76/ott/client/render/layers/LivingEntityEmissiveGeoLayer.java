package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.function.Function;

public class LivingEntityEmissiveGeoLayer<T extends LivingEntity & GeoAnimatable> extends GeoRenderLayer<T> {
    private final Function<ResourceLocation, ResourceLocation> textureFunction;

    public LivingEntityEmissiveGeoLayer(GeoRenderer<T> entityRendererIn) {
        this(entityRendererIn, (texture) -> texture.withPath((path) -> path.replace(".png", "_glow.png")));
    }

    public LivingEntityEmissiveGeoLayer(GeoRenderer<T> entityRendererIn, Function<ResourceLocation, ResourceLocation> textureFunction) {
        super(entityRendererIn);
        this.textureFunction = textureFunction;
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ResourceLocation texture = getTextureResource(animatable);
        ResourceLocation glowTexture = textureFunction.apply(texture);
        
        RenderType glowRenderType = RenderType.entityTranslucentEmissive(glowTexture);

        getRenderer().actuallyRender(poseStack, animatable, model, glowRenderType, bufferSource, bufferSource.getBuffer(glowRenderType), true, partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1);
    }
}
