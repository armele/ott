package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.OtterModel;
import com.otterly76.ott.entity.custom.OtterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class OtterRenderer extends GeoEntityRenderer<OtterEntity> {
    public OtterRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OtterModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public float getMotionAnimThreshold(OtterEntity animatable) {
        return 0.000001f;
    }

    @Override
    public void renderRecursively(PoseStack stack, OtterEntity animatable, @NotNull GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (bone.getName().equals("held_item")) {
            stack.pushPose();
            stack.scale(0.5f, 0.5f, 0.5f);
            stack.translate(0.05D, 0.2D, -0.9D);
            stack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
            stack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F));

            if (animatable.isBaby()) {
                stack.translate(0.0D, -0.6D, 0.0D);
            } else {
                stack.translate(0.0D, -0.125D, 0.0D);
            }

            net.minecraft.client.Minecraft.getInstance().getItemRenderer().renderStatic(animatable.getMainHandItem(), net.minecraft.world.item.ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, packedOverlay, stack, bufferSource, animatable.level(), 0);
            stack.popPose();

            buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(animatable)));
        }
        super.renderRecursively(stack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void preRender(@NotNull PoseStack poseStack, @NotNull OtterEntity animatable, @NotNull BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(OtterEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}