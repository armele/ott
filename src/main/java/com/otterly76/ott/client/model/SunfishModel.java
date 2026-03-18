package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Sunfish;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SunfishModel extends GeoModel<Sunfish> {
    @Override
    public ResourceLocation getModelResource(Sunfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/sunfish/sunfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Sunfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/sunfish/sunfish_0.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Sunfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/sunfish/sunfish.animation.json");
    }
}