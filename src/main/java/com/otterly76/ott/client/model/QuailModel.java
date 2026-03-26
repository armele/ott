package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.QuailEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class QuailModel extends GeoModel<QuailEntity> {
    @Override
    public ResourceLocation getModelResource(QuailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/quail/quail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(QuailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/quail/quail.png");
    }

    @Override
    public ResourceLocation getAnimationResource(QuailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/quail/quail.animation.json");
    }
}
