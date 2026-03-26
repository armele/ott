package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SasquatchModel;
import com.otterly76.ott.entity.custom.SasquatchEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SasquatchRenderer extends GeoEntityRenderer<SasquatchEntity> {
    public SasquatchRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SasquatchModel());
    }

    @Override
    public RenderType getRenderType(SasquatchEntity animatable, ResourceLocation texture, @Nullable net.minecraft.client.renderer.MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}