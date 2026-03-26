package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.RingtailEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RingtailModel extends GeoModel<RingtailEntity> {
    @Override
    public ResourceLocation getModelResource(RingtailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/ringtail/ringtail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RingtailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/ringtail/ringtail.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RingtailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/ringtail/ringtail.animation.json");
    }
}