package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BeaverEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BeaverModel extends GeoModel<BeaverEntity> {
    @Override
    public ResourceLocation getModelResource(BeaverEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/beaver/beaver.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BeaverEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/beaver/beaver.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BeaverEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/beaver/beaver.animation.json");
    }
}