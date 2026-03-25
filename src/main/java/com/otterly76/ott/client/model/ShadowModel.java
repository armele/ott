package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Shadow;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShadowModel extends GeoModel<Shadow> {
    @Override
    public ResourceLocation getModelResource(Shadow animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/shadow/shadow.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Shadow animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/shadow/shadow.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Shadow animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/shadow/shadow.animation.json");
    }
}