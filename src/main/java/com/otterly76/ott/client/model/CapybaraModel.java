package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.CapybaraEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class CapybaraModel extends GeoModel<CapybaraEntity> {
    @Override
    public ResourceLocation getModelResource(CapybaraEntity animatable, GeoRenderer<CapybaraEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/capybara/capybara.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CapybaraEntity animatable, GeoRenderer<CapybaraEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/capybara/capybara.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CapybaraEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/capybara/capybara.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(CapybaraEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(CapybaraEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public void setCustomAnimations(CapybaraEntity animatable, long instanceId, AnimationState<CapybaraEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }
    }
}
