package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.CherryTreeEnt;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CherryTreeEntModel extends GeoModel<CherryTreeEnt> {
    @Override
    public ResourceLocation getModelResource(CherryTreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/cherrytreeent/cherrytreeent.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CherryTreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/cherrytreeent/cherrytreeent.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CherryTreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/cherrytreeent/cherrytreeent.animation.json");
    }
}