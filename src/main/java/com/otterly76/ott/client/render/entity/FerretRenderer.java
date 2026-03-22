package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.FerretModel;
import com.otterly76.ott.client.render.layers.FerretCollarLayer;
import com.otterly76.ott.entity.custom.FerretEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class FerretRenderer extends GeoEntityRenderer<FerretEntity> {
    public FerretRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FerretModel());
        this.addRenderLayer(new FerretCollarLayer(this));
        this.shadowRadius = 0.4F;
    }

    @Override
    public float getMotionAnimThreshold(FerretEntity animatable) {
        return 0.000001f;
    }

    @Override
    public void preRender(@NotNull PoseStack poseStack, @NotNull FerretEntity animatable, @NotNull BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(FerretEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}