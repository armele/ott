package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Snail;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class SnailModel extends GeoModel<Snail> {
    @Override
    public ResourceLocation getModelResource(Snail animatable, GeoRenderer<Snail> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/snail/snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Snail animatable, GeoRenderer<Snail> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/snail/" + animatable.getSnailColor().getName() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Snail animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/snail/snail.animation.json");
    }

    @Override
    public void setCustomAnimations(Snail animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<Snail> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        this.getBone("body").ifPresent(bone -> bone.setHidden(animatable.isHiding()));
    }

    @Override
    public ResourceLocation getModelResource(Snail animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Snail animatable) {
        return getTextureResource(animatable, null);
    }
}