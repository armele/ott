package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GoblinSharkModel;
import com.otterly76.ott.entity.custom.GoblinSharkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GoblinSharkRenderer extends GeoEntityRenderer<GoblinSharkEntity> {
    public GoblinSharkRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GoblinSharkModel());
    }
}
