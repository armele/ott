package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Tortoise;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class TortoiseModel extends GeoModel<Tortoise> {
    @Override
    public ResourceLocation getModelResource(Tortoise animatable, GeoRenderer<Tortoise> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/tortoise/tortoise.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Tortoise animatable, GeoRenderer<Tortoise> renderer) {
        String base = switch (animatable.getVariant()) {
            case 1 -> "green";
            case 2 -> "black";
            default -> "brown";
        };
        String texture = animatable.isBaby() ? base + "_baby" : base;
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/tortoise/" + texture + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Tortoise animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/tortoise/tortoise.animation.json");
    }

    @Override
    public void setCustomAnimations(Tortoise animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<Tortoise> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                this.getBone("head").ifPresent(bone -> {
                    bone.setRotX(entityData.headPitch() * 0.017453292F);
                    bone.setRotY(entityData.netHeadYaw() * 0.017453292F);
                });
            }
        }
    }

    @Override
    public ResourceLocation getModelResource(Tortoise animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Tortoise animatable) {
        return getTextureResource(animatable, null);
    }
}