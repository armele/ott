package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.GiantSoftshellTurtleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GiantSoftshellTurtleModel extends GeoModel<GiantSoftshellTurtleEntity> {
    @Override
    public ResourceLocation getModelResource(GiantSoftshellTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/giant_softshell_turtle/giant_softshell_turtle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GiantSoftshellTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/giant_softshell_turtle/giant_softshell_turtle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GiantSoftshellTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/giant_softshell_turtle/giant_softshell_turtle.animation.json");
    }
}
