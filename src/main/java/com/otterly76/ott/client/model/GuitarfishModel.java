package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.GuitarfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GuitarfishModel extends GeoModel<GuitarfishEntity> {
    @Override
    public ResourceLocation getModelResource(GuitarfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/guitarfish/guitarfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GuitarfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/guitarfish/guitarfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GuitarfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/guitarfish/guitarfish.animation.json");
    }
}
