package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.HowlerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HowlerModel extends GeoModel<HowlerEntity> {
    @Override
    public ResourceLocation getModelResource(HowlerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/howler/howler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HowlerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/howler/howler.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HowlerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/howler/howler.animation.json");
    }
}