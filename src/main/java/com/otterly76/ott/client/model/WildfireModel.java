package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.WildfireEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WildfireModel extends GeoModel<WildfireEntity> {
    @Override
    public ResourceLocation getModelResource(WildfireEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/wildfire/wildfire.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WildfireEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/wildfire/wildfire.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WildfireEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/wildfire/wildfire.animation.json");
    }
}
