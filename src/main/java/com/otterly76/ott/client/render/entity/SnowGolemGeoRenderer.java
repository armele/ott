package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SnowGolemGeoModel;
import com.otterly76.ott.entity.gecko.SnowGolemGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.SnowGolem;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SnowGolemGeoRenderer<T extends SnowGolem & SnowGolemGeoEntity> extends GeoLivingRendererWrapper<T> {
    public SnowGolemGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new SnowGolemGeoModel<>()));
    }
}