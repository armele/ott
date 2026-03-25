package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.CoralSeaViper;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CoralSeaViperModel extends GeoModel<CoralSeaViper> {
    @Override
    public ResourceLocation getModelResource(CoralSeaViper animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/coral_sea_viper/coral_sea_viper.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CoralSeaViper animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/coral_sea_viper/coral_sea_viper.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CoralSeaViper animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/coral_sea_viper/coral_sea_viper.animation.json");
    }
}