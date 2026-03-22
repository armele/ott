package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Shrimp1Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Shrimp1Model extends GeoModel<Shrimp1Entity> {
    @Override
    public ResourceLocation getModelResource(Shrimp1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/shrimp/shrimp_1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Shrimp1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/shrimp/shrimp_1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Shrimp1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/shrimp/shrimp_1.animation.json");
    }
}