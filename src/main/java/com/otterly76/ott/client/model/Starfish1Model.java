package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Starfish1Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Starfish1Model extends GeoModel<Starfish1Entity> {
    @Override
    public ResourceLocation getModelResource(Starfish1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/starfish/starfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Starfish1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/starfish/starfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Starfish1Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/starfish/starfish.animation.json");
    }
}