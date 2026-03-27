package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.RascalModel;
import com.otterly76.ott.entity.custom.RascalEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RascalRenderer extends GeoEntityRenderer<RascalEntity> {
    public RascalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RascalModel());
        this.shadowRadius = 0.5f;
    }
}
