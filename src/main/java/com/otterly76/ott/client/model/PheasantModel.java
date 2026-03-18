package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Pheasant;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PheasantModel extends GeoModel<Pheasant> {
    @Override
    public ResourceLocation getModelResource(Pheasant animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/pheasant/pheasant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Pheasant animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/pheasant/pheasant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Pheasant animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/pheasant/pheasant.animation.json");
    }
}