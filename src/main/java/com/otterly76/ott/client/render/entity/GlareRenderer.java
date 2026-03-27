package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GlareModel;
import com.otterly76.ott.entity.custom.GlareEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GlareRenderer extends GeoEntityRenderer<GlareEntity> {
    public GlareRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GlareModel());
        this.shadowRadius = 0.4f;
    }
}
