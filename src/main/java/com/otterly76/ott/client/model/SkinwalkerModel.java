package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SkinwalkerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SkinwalkerModel extends GeoModel<SkinwalkerEntity> {
    @Override
    public ResourceLocation getModelResource(SkinwalkerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/skinwalker/skinwalker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SkinwalkerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/skinwalker/skinwalker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SkinwalkerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/skinwalker/skinwalker.animation.json");
    }
}