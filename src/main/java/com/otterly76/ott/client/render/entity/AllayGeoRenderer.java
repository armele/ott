package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.AllayGeoModel;
import com.otterly76.ott.entity.gecko.AllayGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.allay.Allay;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AllayGeoRenderer<T extends Allay & AllayGeoEntity> extends GeoLivingRendererWrapper<T> {
    public AllayGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new AllayGeoModel<>()));
    }
}
