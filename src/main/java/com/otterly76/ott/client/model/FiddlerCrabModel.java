package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.FiddlerCrabEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FiddlerCrabModel extends GeoModel<FiddlerCrabEntity> {
    @Override
    public ResourceLocation getModelResource(FiddlerCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/crab/crab.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FiddlerCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/crab/crab.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FiddlerCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/crab/crab.animation.json");
    }
}
