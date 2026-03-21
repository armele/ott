package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Firefly;
import net.minecraft.client.renderer.LightTexture;
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
public class FireflyGlowLayer extends GeoRenderLayer<Firefly> {
    private static final ResourceLocation GLOW_OVERLAY = Constants.loc("textures/entity/firefly/firefly_glow.png");
    private static final ResourceLocation GLOW_EMISSIVE = Constants.loc("textures/entity/firefly/firefly_glow_e.png");

    public FireflyGlowLayer(GeoRenderer<Firefly> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, Firefly entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks, int packedLightIn, int packedOverlay) {
        if (!entity.isGlowing()) {
            return;
        }
        int frame = 30 - Math.min(entity.getGlowTicksRemaining(), 30);

        RenderType overlayType = RenderType.entityTranslucent(GLOW_OVERLAY);
        VertexConsumer overlayBuffer = new FireflyGlowAnim(bufferSource.getBuffer(overlayType), frame);
        getRenderer().reRender(bakedModel, poseStack, bufferSource, entity, overlayType, overlayBuffer, partialTicks, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        RenderType emissiveType = RenderType.entityTranslucentEmissive(GLOW_EMISSIVE);
        VertexConsumer emissiveBuffer = new FireflyGlowAnim(bufferSource.getBuffer(emissiveType), frame);
        getRenderer().reRender(bakedModel, poseStack, bufferSource, entity, emissiveType, emissiveBuffer, partialTicks, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }
}
