package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SeaViper;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SeaViperModel extends GeoModel<SeaViper> {
    @Override
    public ResourceLocation getModelResource(SeaViper animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/sea_viper/sea_viper.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeaViper animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/sea_viper/sea_viper.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeaViper animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/sea_viper/sea_viper.animation.json");
    }
}