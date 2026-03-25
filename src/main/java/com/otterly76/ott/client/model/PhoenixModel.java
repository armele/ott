package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Phoenix;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PhoenixModel extends GeoModel<Phoenix> {
    @Override
    public ResourceLocation getModelResource(Phoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/phoenix/phoenix.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Phoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/phoenix/phoenix.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Phoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/phoenix/phoenix.animation.json");
    }
}