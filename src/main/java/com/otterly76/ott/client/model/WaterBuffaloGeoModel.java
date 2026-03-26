package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.WaterBuffaloEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WaterBuffaloGeoModel extends GeoModel<WaterBuffaloEntity> {
    @Override
    public ResourceLocation getModelResource(WaterBuffaloEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/water_buffalo/water_buffalo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WaterBuffaloEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/water_buffalo/water_buffalo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WaterBuffaloEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/water_buffalo/water_buffalo.animation.json");
    }
}