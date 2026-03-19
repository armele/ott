package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Bass;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BassModel extends GeoModel<Bass> {
    @Override
    public ResourceLocation getModelResource(Bass animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/bass/bass.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Bass animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/bass/bass.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bass animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/bass/bass.animation.json");
    }
}