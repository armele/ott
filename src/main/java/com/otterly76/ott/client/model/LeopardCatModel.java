package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.LeopardCatEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LeopardCatModel extends GeoModel<LeopardCatEntity> {
    @Override
    public ResourceLocation getModelResource(LeopardCatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/leopard_cat/leopard_cat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LeopardCatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/leopard_cat/leopard_cat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LeopardCatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/leopard_cat/leopard_cat.animation.json");
    }
}
