package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.PitViperEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PitViperModel extends GeoModel<PitViperEntity> {
    @Override
    public ResourceLocation getModelResource(PitViperEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/snake_venomous/snake_venomous.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PitViperEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/snake_venomous/pit_viper.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PitViperEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/snake_venomous/snake_venomous.animation.json");
    }
}