package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BewitchedGreywolfEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BewitchedGreywolfModel extends GeoModel<BewitchedGreywolfEntity> {
    @Override
    public ResourceLocation getModelResource(BewitchedGreywolfEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/wolf/wolf.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BewitchedGreywolfEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/skinwalker/bewitched_timber_wolf.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BewitchedGreywolfEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/wolf/wolf.animation.json");
    }
}