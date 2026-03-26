package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.PallasCatEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PallasCatModel extends GeoModel<PallasCatEntity> {
    @Override
    public ResourceLocation getModelResource(PallasCatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/pallas_cat/pallas_cat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PallasCatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pallas_cat/pallas_cat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PallasCatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/pallas_cat/pallas_cat.animation.json");
    }
}
