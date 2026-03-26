package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.TurkeyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TurkeyModel extends GeoModel<TurkeyEntity> {
    @Override
    public ResourceLocation getModelResource(TurkeyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/turkey/turkey.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TurkeyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/turkey/turkey.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TurkeyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/turkey/turkey.animation.json");
    }
}