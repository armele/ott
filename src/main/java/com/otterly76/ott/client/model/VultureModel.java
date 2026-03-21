package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Vulture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class VultureModel extends GeoModel<Vulture> {
    @Override
    public ResourceLocation getModelResource(Vulture animatable, GeoRenderer<Vulture> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/vulture/vulture.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Vulture animatable, GeoRenderer<Vulture> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/vulture/vulture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Vulture animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/vulture/vulture.animation.json");
    }

    @Override
    public void setCustomAnimations(Vulture animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<Vulture> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            this.getBone("head").ifPresent(bone -> {
                assert entityData != null;
                bone.setRotX(entityData.headPitch() * 0.017453292F);
                bone.setRotY(entityData.netHeadYaw() * 0.017453292F);
            });
        }
    }

    @Override
    public ResourceLocation getModelResource(Vulture animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Vulture animatable) {
        return getTextureResource(animatable, null);
    }
}
