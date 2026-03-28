package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.CoconutCrabEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CoconutCrabModel extends GeoModel<CoconutCrabEntity> {
    @Override
    public ResourceLocation getModelResource(CoconutCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/coconut_crab/coconut_crab.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CoconutCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/coconut_crab/coconut_crab.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CoconutCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/coconut_crab/coconut_crab.animation.json");
    }
}
