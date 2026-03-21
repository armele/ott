package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.model.BirdModel;
import com.otterly76.ott.entity.custom.Bird;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BirdRenderer extends GeoEntityRenderer<Bird> {
    public BirdRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BirdModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    protected void applyRotations(Bird animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if (animatable.isPassenger()) {
            poseStack.translate(0.0D, -0.1D, 0.0D);
        }
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
