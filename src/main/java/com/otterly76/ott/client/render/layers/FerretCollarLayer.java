package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.FerretEntity;
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
public class FerretCollarLayer extends GeoRenderLayer<FerretEntity> {

    public FerretCollarLayer(GeoRenderer<FerretEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, FerretEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.isTame() && !animatable.isInvisible()) {
            var color = animatable.getCollarColor();
            if (color != null) {
                String name = animatable.isBaby() ? "baby_ferret_tamed_overlay_" : "ferret_tamed_overlay_";
                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/ferret/" + name + color.getName() + ".png");
                RenderType layer = RenderType.entityCutoutNoCull(texture);
                this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, layer, bufferSource.getBuffer(layer), partialTick, packedLight, OverlayTexture.NO_OVERLAY, -1);
            }
        }
    }
}