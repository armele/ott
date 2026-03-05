package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.RabbitGeoModel;
import com.otterly76.ott.entity.gecko.RabbitGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.Rabbit;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RabbitGeoRenderer<T extends Rabbit & RabbitGeoEntity> extends GeoEntityRenderer<T> {
    public RabbitGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RabbitGeoModel<>());
    }
}