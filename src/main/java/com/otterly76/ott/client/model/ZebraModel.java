package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Zebra;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ZebraModel extends GeoModel<Zebra> {
    @Override
    public ResourceLocation getModelResource(Zebra animatable, GeoRenderer<Zebra> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/zebra/zebra.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Zebra animatable, GeoRenderer<Zebra> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/zebra/zebra.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Zebra animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/zebra/zebra.animation.json");
    }

    @Override
    public void setCustomAnimations(Zebra animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<Zebra> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                this.getBone("skull").ifPresent(bone -> {
                    bone.setRotX(entityData.headPitch() * 0.017453292F);
                    bone.setRotY(entityData.netHeadYaw() * 0.017453292F);
                });
            }
            this.getBone("saddle").ifPresent(bone -> bone.setHidden(!animatable.isSaddled()));
            this.getBone("bridle").ifPresent(bone -> bone.setHidden(!animatable.isSaddled()));
            this.getBone("reinsL").ifPresent(bone -> bone.setHidden(!animatable.isSaddled()));
            this.getBone("reinsR").ifPresent(bone -> bone.setHidden(!animatable.isSaddled()));
            this.getBone("chest").ifPresent(bone -> bone.setHidden(!animatable.hasChest()));
        }
    }

    @Override
    public ResourceLocation getModelResource(Zebra animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Zebra animatable) {
        return getTextureResource(animatable, null);
    }
}