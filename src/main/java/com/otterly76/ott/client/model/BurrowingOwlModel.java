package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BurrowingOwlEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BurrowingOwlModel extends GeoModel<BurrowingOwlEntity> {
    @Override
    public ResourceLocation getModelResource(BurrowingOwlEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/burrowing_owl/burrowing_owl.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BurrowingOwlEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/burrowing_owl/burrowing_owl.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BurrowingOwlEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/burrowing_owl/burrowing_owl.animation.json");
    }
}
