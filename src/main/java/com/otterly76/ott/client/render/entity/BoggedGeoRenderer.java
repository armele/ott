package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BoggedGeoModel;
import com.otterly76.ott.entity.gecko.BoggedGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Bogged;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoggedGeoRenderer<T extends Bogged & BoggedGeoEntity> extends GeoLivingRendererWrapper<T> {
    public BoggedGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new BoggedGeoModel<>()));
    }
}