package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.model.SmallJellyfishModel;
import com.otterly76.ott.entity.custom.SmallJellyfishEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SmallJellyfishRenderer extends GeoEntityRenderer<SmallJellyfishEntity> {
    public SmallJellyfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SmallJellyfishModel());
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void render(@NotNull SmallJellyfishEntity animatable, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }
}
