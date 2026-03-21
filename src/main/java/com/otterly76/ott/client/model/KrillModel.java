package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Krill;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class KrillModel extends GeoModel<Krill> {
    @Override
    public ResourceLocation getModelResource(Krill animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/krill/krill.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Krill animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/krill/krill.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Krill animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/krill/krill.animation.json");
    }

    @Override
    public void setCustomAnimations(Krill animatable, long instanceId, AnimationState<Krill> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
