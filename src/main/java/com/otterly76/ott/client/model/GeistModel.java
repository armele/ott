package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Geist;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GeistModel extends GeoModel<Geist> {
    @Override
    public ResourceLocation getModelResource(Geist animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/geist/geist.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Geist animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/geist/geist.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Geist animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/geist/geist.animation.json");
    }
}