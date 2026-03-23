package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.Constants;
import com.otterly76.ott.block.entity.ButterflyJarBlockEntity;
import com.otterly76.ott.entity.custom.Butterfly;
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
public class ButterflyJarGlowLayer extends GeoRenderLayer<ButterflyJarBlockEntity> {
    public ButterflyJarGlowLayer(GeoRenderer<ButterflyJarBlockEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, ButterflyJarBlockEntity entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks, int packedLightIn, int packedOverlay) {
        ResourceLocation glowTexture = getGlowTexture(entity.getVariant());
        if (glowTexture == null) {
            return;
        }

        RenderType emissiveType = RenderType.entityTranslucentEmissive(glowTexture);
        getRenderer().reRender(bakedModel, poseStack, bufferSource, entity, emissiveType, bufferSource.getBuffer(emissiveType), partialTicks, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    @org.jetbrains.annotations.Nullable
    private ResourceLocation getGlowTexture(Butterfly.Variant variant) {
        return switch (variant) {
            case CHORUSMORPHO -> Constants.loc("textures/entity/butterfly/chorusbfglow.png");
            case ENDERFLY -> Constants.loc("textures/entity/butterfly/enderflyglow.png");
            case GLOWSTONEBF -> Constants.loc("textures/entity/butterfly/glowstonebfglow.png");
            case SOULBF -> Constants.loc("textures/entity/butterfly/soulbfglow.png");
            default -> null;
        };
    }
}