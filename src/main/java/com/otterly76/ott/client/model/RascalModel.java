package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.RascalEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RascalModel extends GeoModel<RascalEntity> {
    @Override
    public ResourceLocation getModelResource(RascalEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/rascal/rascal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RascalEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/rascal/rascal.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RascalEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/rascal/rascal.animation.json");
    }
}
