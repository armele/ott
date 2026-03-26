package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.CichlidEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CichlidModel extends GeoModel<CichlidEntity> {
    @Override
    public ResourceLocation getModelResource(CichlidEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/cichlid/cichlid.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CichlidEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/cichlid/cichlid.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CichlidEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/cichlid/cichlid.animation.json");
    }
}
