package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Angelfish;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AngelfishModel extends GeoModel<Angelfish> {
    @Override
    public ResourceLocation getModelResource(Angelfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/angelfish/angelfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Angelfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/angelfish/angelfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Angelfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/angelfish/angelfish.animation.json");
    }
}
