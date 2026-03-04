package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.otterly76.ott.client.model.ManOWarModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.ManOWar;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ManOWarRenderer<T extends ManOWar> extends GeoEntityRenderer<T> {
    public ManOWarRenderer(EntityRendererProvider.Context context) {
        super(context, new ManOWarModel<>());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this,
            (texture) -> texture.withPath((path) -> path.replace(".png", "_glowmask.png"))));
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        float i = Mth.lerp(partialTick, animatable.xBodyRotO, animatable.xBodyRot);
        float j = Mth.lerp(partialTick, animatable.zBodyRotO, animatable.zBodyRot);
        poseStack.translate(0.0F, 0.5F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(i));
        poseStack.mulPose(Axis.YP.rotationDegrees(j));
        poseStack.translate(0.0F, -1.2F, 0.0F);
    }

    @Override
    protected float getShadowRadius(@NotNull T entity) {
        return entity.isBaby() ? 0.5F : 0.8F;
    }
}