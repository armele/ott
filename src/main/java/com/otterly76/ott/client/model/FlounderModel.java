package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Flounder;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FlounderModel extends GeoModel<Flounder> {
    @Override
    public ResourceLocation getModelResource(Flounder animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/flounder/flounder.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Flounder animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/flounder/flounder.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Flounder animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/flounder/flounder.animation.json");
    }
}
