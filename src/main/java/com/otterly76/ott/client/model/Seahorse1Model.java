package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Seahorse1Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Seahorse1Model extends GeoModel<Seahorse1Entity> {
    @Override
    public ResourceLocation getModelResource(Seahorse1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/seahorse/seahorse.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Seahorse1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/seahorse/seahorse.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Seahorse1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/seahorse/seahorse.animation.json");
    }
}
