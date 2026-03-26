package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BeaverModel;
import com.otterly76.ott.entity.custom.BeaverEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BeaverRenderer extends GeoEntityRenderer<BeaverEntity> {
    public BeaverRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BeaverModel());
    }
}