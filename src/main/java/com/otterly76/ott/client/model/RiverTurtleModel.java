package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.RiverTurtleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RiverTurtleModel extends GeoModel<RiverTurtleEntity> {
    @Override
    public ResourceLocation getModelResource(RiverTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/river_turtle/river_turtle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RiverTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/river_turtle/river_turtle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RiverTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/river_turtle/river_turtle.animation.json");
    }
}
