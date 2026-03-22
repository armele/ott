package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.JumpingSpiderModel;
import com.otterly76.ott.entity.custom.JumpingSpiderEntity;
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
public class JumpingSpiderRenderer extends GeoEntityRenderer<JumpingSpiderEntity> {
    public JumpingSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new JumpingSpiderModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public float getMotionAnimThreshold(JumpingSpiderEntity animatable) {
        return 0.000001f;
    }

    @Override
    public void preRender(@NotNull PoseStack poseStack, @NotNull JumpingSpiderEntity animatable, @NotNull BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(JumpingSpiderEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}