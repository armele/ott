package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.KiwiEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class KiwiModel extends GeoModel<KiwiEntity> {
    @Override
    public ResourceLocation getModelResource(KiwiEntity animatable, GeoRenderer<KiwiEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/kiwi/kiwi.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KiwiEntity animatable, GeoRenderer<KiwiEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/kiwi/kiwi.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KiwiEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/kiwi/kiwi.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(KiwiEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(KiwiEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public void setCustomAnimations(KiwiEntity animatable, long instanceId, AnimationState<KiwiEntity> animationState) {
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