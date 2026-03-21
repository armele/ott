package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.VexGeoModel;
import com.otterly76.ott.entity.gecko.VexGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Vex;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VexGeoRenderer<T extends Vex & VexGeoEntity> extends GeoLivingRendererWrapper<T> {
    public VexGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new VexGeoModel<>()));
    }
}
