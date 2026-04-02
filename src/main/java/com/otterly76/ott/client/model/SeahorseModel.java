package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SeahorseEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SeahorseModel extends GeoModel<SeahorseEntity> {
    @Override
    public ResourceLocation getModelResource(SeahorseEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/seahorse/seahorse.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeahorseEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/seahorse/seahorse.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeahorseEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/seahorse/seahorse.animation.json");
    }
}
