package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.DrownedGeoModel;
import com.otterly76.ott.entity.gecko.DrownedGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Drowned;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrownedGeoRenderer<T extends Drowned & DrownedGeoEntity> extends GeoEntityRenderer<T> {
    public DrownedGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DrownedGeoModel<>());
    }
}