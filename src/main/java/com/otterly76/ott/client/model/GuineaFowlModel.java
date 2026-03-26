package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.GuineaFowlEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GuineaFowlModel extends GeoModel<GuineaFowlEntity> {
    @Override
    public ResourceLocation getModelResource(GuineaFowlEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/guinea_fowl/guinea_fowl.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GuineaFowlEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/guinea_fowl/guinea_fowl.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GuineaFowlEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/guinea_fowl/guinea_fowl.animation.json");
    }
}
