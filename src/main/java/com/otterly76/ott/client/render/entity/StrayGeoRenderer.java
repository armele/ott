package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.StrayGeoModel;
import com.otterly76.ott.entity.gecko.StrayGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Stray;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StrayGeoRenderer<T extends Stray & StrayGeoEntity> extends GeoEntityRenderer<T> {
    public StrayGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StrayGeoModel<>());
    }
}