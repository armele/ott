package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.entity.core.SleepingAnimal;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class SleepLayer<T extends LivingEntity & GeoAnimatable & SleepingAnimal> extends GeoRenderLayer<T> {
    private final ResourceLocation sleepLayer;

    public SleepLayer(GeoRenderer<T> entityRendererIn, ResourceLocation sleepLayer) {
        super(entityRendererIn);
        this.sleepLayer = sleepLayer;
    }

    @Override
    public void render(PoseStack poseStack, @NotNull T entity, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks,
                       int packedLightIn, int packedOverlay) {

        if (entity.isSleeping()) {
            RenderType renderLayer = RenderType.entityCutoutNoCull(sleepLayer);
            getRenderer().reRender(getDefaultBakedModel(entity), poseStack, bufferSource, entity, renderLayer, bufferSource.getBuffer(renderLayer), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }
    }

}
