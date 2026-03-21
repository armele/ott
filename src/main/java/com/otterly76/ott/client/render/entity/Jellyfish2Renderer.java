package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.model.Jellyfish2Model;
import com.otterly76.ott.entity.custom.Jellyfish2Entity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Jellyfish2Renderer extends GeoEntityRenderer<Jellyfish2Entity> {
    public Jellyfish2Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Jellyfish2Model());
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void render(@NotNull Jellyfish2Entity animatable, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }
}
