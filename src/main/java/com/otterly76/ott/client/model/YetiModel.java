package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Yeti;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class YetiModel extends GeoModel<Yeti> {
    @Override
    public ResourceLocation getModelResource(Yeti animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/yeti/yeti.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Yeti animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/yeti/yeti.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Yeti animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/yeti/yeti.animation.json");
    }
}