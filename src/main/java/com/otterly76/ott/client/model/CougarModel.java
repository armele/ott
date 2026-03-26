package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.CougarEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CougarModel extends GeoModel<CougarEntity> {
    @Override
    public ResourceLocation getModelResource(CougarEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/cougar/cougar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CougarEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/cougar/cougar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CougarEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/cougar/cougar.animation.json");
    }
}