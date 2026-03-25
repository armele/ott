package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.AridYeti;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AridYetiModel extends GeoModel<AridYeti> {
    @Override
    public ResourceLocation getModelResource(AridYeti animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/arid_yeti/arid_yeti.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AridYeti animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/arid_yeti/arid_yeti.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AridYeti animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/arid_yeti/arid_yeti.animation.json");
    }
}