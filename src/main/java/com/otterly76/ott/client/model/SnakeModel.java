package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SnakeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SnakeModel extends GeoModel<SnakeEntity> {
    @Override
    public ResourceLocation getModelResource(SnakeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/snake/snake.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SnakeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/snake/garter_snake.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SnakeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/snake/snake.animation.json");
    }
}