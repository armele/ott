package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.RattlesnakeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RattlesnakeModel extends GeoModel<RattlesnakeEntity> {
    @Override
    public ResourceLocation getModelResource(RattlesnakeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/snake_venomous/snake_venomous.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RattlesnakeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/snake_venomous/rattlesnake.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RattlesnakeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/snake_venomous/snake_venomous.animation.json");
    }
}