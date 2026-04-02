package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.StarfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StarfishModel extends GeoModel<StarfishEntity> {
    @Override
    public ResourceLocation getModelResource(StarfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/starfish/starfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StarfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/starfish/starfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StarfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/starfish/starfish.animation.json");
    }
}
