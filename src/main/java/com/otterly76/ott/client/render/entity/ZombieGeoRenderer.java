package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.ZombieGeoModel;
import com.otterly76.ott.entity.gecko.ZombieGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Zombie;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZombieGeoRenderer<T extends Zombie & ZombieGeoEntity> extends GeoLivingRendererWrapper<T> {
    public ZombieGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new ZombieGeoModel<>()));
    }
}
