package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SkeletonGeoModel;
import com.otterly76.ott.entity.gecko.SkeletonGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Skeleton;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SkeletonGeoRenderer<T extends Skeleton & SkeletonGeoEntity> extends GeoLivingRendererWrapper<T> {
    public SkeletonGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new SkeletonGeoModel<>()));
    }
}