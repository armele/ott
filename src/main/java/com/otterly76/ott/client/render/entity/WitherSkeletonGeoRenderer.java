package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.WitherSkeletonGeoModel;
import com.otterly76.ott.entity.gecko.WitherSkeletonGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.WitherSkeleton;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WitherSkeletonGeoRenderer<T extends WitherSkeleton & WitherSkeletonGeoEntity> extends GeoLivingRendererWrapper<T> {
    public WitherSkeletonGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoEntityRenderer<>(renderManager, new WitherSkeletonGeoModel<>()));
    }
}
