package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.WaterBuffaloGeoModel;
import com.otterly76.ott.entity.custom.WaterBuffaloEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WaterBuffaloGeoRenderer extends GeoLivingRendererWrapper<WaterBuffaloEntity> {
    public WaterBuffaloGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new WaterBuffaloGeoModel()));
    }
}