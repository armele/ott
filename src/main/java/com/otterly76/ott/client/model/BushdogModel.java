package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BushdogEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BushdogModel extends GeoModel<BushdogEntity> {
    @Override
    public ResourceLocation getModelResource(BushdogEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/bushdog/bushdog.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BushdogEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/bushdog/bushdog.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BushdogEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/bushdog/bushdog.animation.json");
    }
}
