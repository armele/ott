package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.client.model.LargeJellyfishModel;
import com.otterly76.ott.entity.custom.LargeJellyfishEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LargeJellyfishRenderer extends GeoEntityRenderer<LargeJellyfishEntity> {
    public LargeJellyfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LargeJellyfishModel());
    }

    @Override
    public RenderType getRenderType(LargeJellyfishEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void render(@NotNull LargeJellyfishEntity animatable, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }

    @Override
    protected void applyRotations(@NotNull LargeJellyfishEntity entity, @NotNull PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float scale) {
        super.applyRotations(entity, poseStack, ageInTicks, rotationYaw, partialTick, scale);

        var look = entity.getViewVector(partialTick);
        var hor = Math.sqrt(look.x * look.x + look.z * look.z);
        var pitch = (float) Math.toDegrees(Math.atan2(look.y, hor));

        poseStack.translate(0, 0.5F, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch - 90F));
    }
}
