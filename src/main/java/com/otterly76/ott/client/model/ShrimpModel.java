package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.ShrimpEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShrimpModel extends GeoModel<ShrimpEntity> {
    @Override
    public ResourceLocation getModelResource(ShrimpEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/shrimp/shrimp.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShrimpEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/shrimp/shrimp.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShrimpEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/shrimp/shrimp.animation.json");
    }
}
