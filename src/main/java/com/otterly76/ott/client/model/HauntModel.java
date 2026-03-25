package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Haunt;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HauntModel extends GeoModel<Haunt> {
    @Override
    public ResourceLocation getModelResource(Haunt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/haunt/haunt.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Haunt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/haunt/haunt.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Haunt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/haunt/haunt.animation.json");
    }
}