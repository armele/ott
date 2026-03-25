package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BoggedShadow;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoggedShadowModel extends GeoModel<BoggedShadow> {
    @Override
    public ResourceLocation getModelResource(BoggedShadow animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/bogged_shadow/bogged_shadow.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BoggedShadow animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/bogged_shadow/bogged_shadow.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BoggedShadow animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/bogged_shadow/bogged_shadow.animation.json");
    }
}