package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Moose;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MooseModel extends GeoModel<Moose> {
    @Override
    public ResourceLocation getModelResource(Moose animatable, GeoRenderer<Moose> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/moose/moose.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Moose animatable, GeoRenderer<Moose> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/moose/moose.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Moose animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/moose/moose.animation.json");
    }

    @Override
    public void setCustomAnimations(Moose animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<Moose> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                this.getBone("head").ifPresent(bone -> {
                    bone.setRotX(entityData.headPitch() * 0.017453292F);
                    bone.setRotY(entityData.netHeadYaw() * 0.017453292F);
                });
            }
            this.getBone("saddle").ifPresent(bone -> bone.setHidden(!animatable.isSaddled()));
        }
    }

    @Override
    public ResourceLocation getModelResource(Moose animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Moose animatable) {
        return getTextureResource(animatable, null);
    }
}