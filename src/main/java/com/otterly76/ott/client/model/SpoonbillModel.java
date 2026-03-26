package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SpoonbillEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpoonbillModel extends GeoModel<SpoonbillEntity> {
    @Override
    public ResourceLocation getModelResource(SpoonbillEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/spoonbill/spoonbill.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpoonbillEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/spoonbill/spoonbill.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SpoonbillEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/spoonbill/spoonbill.animation.json");
    }
}
