package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.WolverineEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WolverineModel extends GeoModel<WolverineEntity> {
    @Override
    public ResourceLocation getModelResource(WolverineEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/wolverine/wolverine.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WolverineEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/wolverine/wolverine.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WolverineEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/wolverine/wolverine.animation.json");
    }
}