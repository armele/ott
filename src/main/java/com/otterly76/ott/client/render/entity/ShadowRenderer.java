package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.ShadowModel;
import com.otterly76.ott.entity.custom.Shadow;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShadowRenderer extends GeoEntityRenderer<Shadow> {
    public ShadowRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShadowModel());
    }
}