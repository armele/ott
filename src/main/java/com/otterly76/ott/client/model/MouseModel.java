package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.MouseEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MouseModel extends GeoModel<MouseEntity> {
    @Override
    public ResourceLocation getModelResource(MouseEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/mouse/mouse.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MouseEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/mouse/mouse.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MouseEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/mouse/mouse.animation.json");
    }
}