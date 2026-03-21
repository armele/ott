package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Mammoth;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MammothModel extends GeoModel<Mammoth> {
    @Override
    public ResourceLocation getModelResource(Mammoth animatable, GeoRenderer<Mammoth> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/mammoth/mammoth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Mammoth animatable, GeoRenderer<Mammoth> renderer) {
        if (animatable.getVariant() == 1) {
            return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/mammoth/mycelium_mammoth.png");
        }
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/mammoth/mammoth.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Mammoth animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/mammoth/mammoth.animation.json");
    }

    @Override
    public void setCustomAnimations(Mammoth animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<Mammoth> animationState) {
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
            this.getBone("chests").ifPresent(bone -> bone.setHidden(true)); // For now hide them as they are not implemented
        }
    }

    @Override
    public ResourceLocation getModelResource(Mammoth animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Mammoth animatable) {
        return getTextureResource(animatable, null);
    }
}