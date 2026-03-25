package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Ghost;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GhostModel extends GeoModel<Ghost> {
    @Override
    public ResourceLocation getModelResource(Ghost animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/ghost/ghost.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Ghost animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/ghost/ghost.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Ghost animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/ghost/ghost.animation.json");
    }
}