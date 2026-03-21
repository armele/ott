package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.HuskGeoModel;
import com.otterly76.ott.entity.gecko.HuskGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Husk;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HuskGeoRenderer<T extends Husk & HuskGeoEntity> extends GeoLivingRendererWrapper<T> {
    public HuskGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new HuskGeoModel<>()));
    }
}
